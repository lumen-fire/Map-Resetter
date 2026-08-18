package me.lumen.mapResetter;

import me.lumen.mapResetterAPI.CreationError;

import org.bukkit.World;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Stream;

/**
 * The internal implementation of {@link me.lumen.mapResetterAPI.MapResetManager}
 */
public class MapResetManager implements me.lumen.mapResetterAPI.MapResetManager {
    private static MapResetManager instance;
    private static final File mapSaveFolder = new File(MapResetter.getPlugin().getDataFolder(), "MapSaves");
    private static final HashMap<String, me.lumen.mapResetterAPI.MapSave> mapSaves = new HashMap<>();

    private MapResetManager(){}

    /**
     * Get or create the singleton
     * @return the internal instance of the map reset manager
     */
    public static MapResetManager getInstance() {
        if (instance == null) {
            instance = new MapResetManager();
        }
        return instance;
    }

    @Override
    public @NonNull Collection<String> getMapSaveIds() {
        return mapSaves.keySet();
    }

    @Override
    public @NonNull Optional<me.lumen.mapResetterAPI.MapSave> getMapSave(String id) {
        me.lumen.mapResetterAPI.MapSave mapSave = mapSaves.get(id);
        if (mapSave == null) {
            return Optional.empty();
        }
        return Optional.of(mapSave);
    }

    @Override
    public @Nullable CreationError createMapSave(@NonNull String id, World loadFrom) {
        CreationError error = CreationError.getError(id);
        if (error != null) {
            return error;
        }

        File target = new File(mapSaveFolder, id);
        MapSave mapSave = new MapSave(id);
        loadFrom.save();
        copyDirectory(loadFrom.getWorldFolder(), target);
        mapSaves.put(id, mapSave);

        return null;
    }

    @Override
    public void deleteMapSave(me.lumen.mapResetterAPI.@NonNull MapSave mapSave) {
        //delete the saves directory
        File directory = new File(mapSaveFolder, mapSave.getId());
        deleteDirectory(directory);

        mapSaves.remove(mapSave.getId());
    }

    public static File getMapSaveFolder() {
        return mapSaveFolder;
    }

    @Override
    public void reloadMapSaves(){
        mapSaveFolder.mkdirs();
        try (Stream<Path> stream = Files.list(mapSaveFolder.toPath())){
            mapSaves.clear();
            stream.filter(path -> !path.equals(mapSaveFolder.toPath())).forEach(path -> mapSaves.put(path.getFileName().toString(), new MapSave(path.getFileName().toString())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //version 1 - hopefully fast enough - may need to use os native commands or something to make it faster
    public static void copyDirectory(@NonNull File source, @NonNull File target){
        //firstly clean the target
        if (target.exists()) {
            deleteDirectory(target);
        }

        //then copy the source to the location
        try {
            Files.walkFileTree(source.toPath(), new SimpleFileVisitor<>(){
                @Override
                public @NonNull FileVisitResult preVisitDirectory(@NonNull Path dir, @NonNull BasicFileAttributes attrs) throws IOException {
                    Path targetDir = target.toPath().resolve(source.toPath().relativize(dir));
                    if (!Files.exists(targetDir)) {
                        Files.createDirectories(targetDir);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public @NonNull FileVisitResult visitFile(@NonNull Path file, @NonNull BasicFileAttributes attrs) throws IOException {
                    Path targetFile = target.toPath().resolve(source.toPath().relativize(file));
                    if (targetFile.toString().contains("metadata.dat") || targetFile.toString().contains("uid.dat") || targetFile.toString().contains("session.lock")) {
                        //skip if the name contains one of the files that may cause it to lock
                        if (MapResetter.getPlugin().getConfig().getBoolean("debug")) {
                            MapResetter.getPlugin().getLogger().info("Skipping copying file " + targetFile);
                        }
                        return FileVisitResult.CONTINUE;
                    }
                    Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete a directory, for example when deleting a map save or removing a world before resetting it
     * @param directory the directory to delete
     */
    public static void deleteDirectory(@NonNull File directory){
        try {
            Files.walkFileTree(directory.toPath(), new SimpleFileVisitor<>() {
                @Override
                public @NonNull FileVisitResult visitFile(@NonNull Path file, @NonNull BasicFileAttributes attributes) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public @NonNull FileVisitResult postVisitDirectory(@NonNull Path directory, IOException ioe) throws IOException {
                    String os = System.getProperty("os.name").toLowerCase();
                    if (os.contains("win")) {
                        Files.setAttribute(directory, "dos:readonly", false);
                    }
                    Files.delete(directory);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
