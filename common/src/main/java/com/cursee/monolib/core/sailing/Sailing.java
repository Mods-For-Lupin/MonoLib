package com.cursee.monolib.core.sailing;

import com.cursee.monolib.core.sailing.warden.SailingWarden;
import com.cursee.monolib.platform.Services;
import io.github.jason13official.monolib.Constants;
import io.github.jason13official.monolib.MonoLib;
import io.github.jason13official.monolib.impl.common.ModConfig;
import io.github.jason13official.monolib.impl.common.util.GsonConfigMapper;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;
import org.apache.commons.lang3.ArrayUtils;
import oshi.util.tuples.Pair;
import oshi.util.tuples.Triplet;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Adapted from Serilum's <a href="https://github.com/Serilum/Collective">Collective</a>, to advocate against using reposted mods.
 * <br />
 * Checks installed mods against registered mods to identify
 */
@Deprecated
public class Sailing {

    public record SailingEntry(String modID, String modName, String modVersion, String modPublisher, String modURL) {}
    
    private static final String CHECKED_TAG = Constants.MOD_ID + ".checked";
    private static final AtomicBoolean UNVERIFIED_INSTANCE = new AtomicBoolean(true);

    private static final Map<String, String> FILENAME_TO_MOD_NAME_MAP = new HashMap<String, String>();
    private static final Map<String, SailingEntry> MOD_NAME_TO_ENTRY_MAP = new HashMap<String, SailingEntry>();

    @Deprecated(since = "2.0.0", forRemoval = true)
    public static void register(String modName, String modID, String modVersion, String minecraftVersion, Pair<String, String> publisherAuthorPair, Triplet<String, String, String> modURLTriplet) {
        register(modID, modName, modVersion, publisherAuthorPair.getA(), modURLTriplet.getA());
    }

    @Deprecated
    public static void register(String modID, String modName, String modVersion, String modPublisher, String modURL) {

        String filename = modID + '-' + "merged" + '-' + SharedConstants.VERSION_STRING + '-' + modVersion + ".jar";
        // FILENAME_TO_MOD_NAME_MAP.put(filename, modName);

        // final SailingEntry sailingEntry = new SailingEntry(modID, modName, modVersion, modPublisher, modURL);
        // MOD_NAME_TO_ENTRY_MAP.put(modName, sailingEntry);

        io.github.jason13official.monolib.impl.common.sailing.Sailing.register(modID, filename);
    }

    @Deprecated
    public static void onEntityJoinLevel(Entity entity, Level level) {
        
        final boolean ENTITY_IS_PLAYER = entity instanceof Player;

        // final boolean VERIFY_JAR_CONFIG_VALUE = CommonConfigValues.enable_jar_verification;
        final boolean VERIFY_JAR_CONFIG_VALUE = ((ModConfig) GsonConfigMapper.get(MonoLib.identifier("server"))).verifyModFilenames;

        final boolean INSTANCE_UNVERIFIED = UNVERIFIED_INSTANCE.get();
        final boolean ENTITY_CHECKED = entity.getTags().contains(CHECKED_TAG);
        
        if (!ENTITY_IS_PLAYER || !VERIFY_JAR_CONFIG_VALUE || !INSTANCE_UNVERIFIED || ENTITY_CHECKED) return;
        
        Player player = (Player) entity;

        if (!SailingWarden.UNSAFE_PATH_TO_UNSAFE_HOST_MAP.isEmpty()) {
            sendMessage(player, "Unsafe download(s):", ChatFormatting.WHITE);
            Constants.LOG.info("Unsafe download(s):");
            for (String key : SailingWarden.UNSAFE_PATH_TO_UNSAFE_HOST_MAP.keySet()) {
                sendMessage(player, "- " + key + " from " + SailingWarden.UNSAFE_PATH_TO_UNSAFE_HOST_MAP.get(key), ChatFormatting.WHITE);
                Constants.LOG.info("- {} from {}", key, SailingWarden.UNSAFE_PATH_TO_UNSAFE_HOST_MAP.get(key));
            }
        }

        verifyJarFiles(player, level);
        entity.addTag(CHECKED_TAG);
    }

    @Deprecated
    private static void verifyJarFiles(Player player, Level level) {
        
        if (!(level instanceof ServerLevel)) return;
        
        List<String> MOD_NAMES_MISSING_JAR_FILE = getModNamesMissingJarFile();
        
        if (!MOD_NAMES_MISSING_JAR_FILE.isEmpty() && preVerificationProcess(level)) {

            sendMessage(player, "Mod(s) from incorrect sources:", ChatFormatting.RED);
            for (String modName : MOD_NAMES_MISSING_JAR_FILE) {
                SailingEntry entry = MOD_NAME_TO_ENTRY_MAP.get(modName);
                sendMessage(player, modName + " by " + entry.modPublisher() + " (Click Here)", ChatFormatting.YELLOW, entry.modURL());
            }

            sendMessage(player, "You a receiving this message because one or more of your mod files has been altered and possibly not downloaded from an original and safe source. Unofficial sources can contain malicious software or host outdated versions of mods, as well as removing ad revenue from mod authors.", ChatFormatting.RED);
            sendMessage(player, "Click on the name of the mod above to find it's original posting.", ChatFormatting.DARK_GREEN);
            sendMessage(player, "You won't see this message again in this instance. Thank you for reading.", ChatFormatting.DARK_GREEN);

            Constants.LOG.info("You a receiving this message because one or more of your mod files has been altered and possibly not downloaded from an original and safe source. Unofficial sources can contain malicious software or host outdated versions of mods, as well as removing ad revenue from mod authors.");
            Constants.LOG.info("Click on the name of the mod above to find it's original posting.");
            Constants.LOG.info("You won't see this message again in this instance. Thank you for reading.");

            postVerificationProcess(level);
        }
        
        UNVERIFIED_INSTANCE.set(false);
    }

    @Deprecated
    private static boolean preVerificationProcess(Level level) {

        String path = ((ServerLevel) level).getServer().getWorldPath(LevelResource.ROOT).toString();
        path = (path.substring(0, path.length() - 2)) + File.separator + "config" + File.separator + Constants.MOD_ID + File.separator + "checked.txt"; // world_save/config/monolib
        File checkFile = new File(path);

        String alternativePath = Services.PLATFORM.getGameDirectory() + File.separator + "config" + File.separator + Constants.MOD_ID + File.separator + "checked.txt"; // game_directory/config/monolib
        File alternativeFile = new File(alternativePath);

        if (checkFile.exists()) UNVERIFIED_INSTANCE.set(false);
        else if (alternativeFile.exists()) UNVERIFIED_INSTANCE.set(false);
        
        return UNVERIFIED_INSTANCE.get();
    }

    @Deprecated
    private static void postVerificationProcess(Level level) {
        
        UNVERIFIED_INSTANCE.set(false);

        // check world save config file
        String path = ((ServerLevel) level).getServer().getWorldPath(LevelResource.ROOT).toString();
        path = (path.substring(0, path.length() - 2)) + File.separator + "config" + File.separator + Constants.MOD_ID; // world_save/config/monolib

        File directory = new File(path);
        if (!directory.mkdirs()) return;
        try (PrintWriter writer = new PrintWriter(path + File.separator + "checked.txt", StandardCharsets.UTF_8);) {
            writer.println("# Please check out https://stopmodreposts.org/ for more information on why this feature exists.");
            writer.println("checked=true");
        } catch (Exception ignored) {}

        // check root game directory config file
        String alternativePath = Services.PLATFORM.getGameDirectory();
        alternativePath = alternativePath + File.separator + "config" + File.separator + Constants.MOD_ID; // game_directory/config/monolib
        if (new File(alternativePath + File.separator + "checked.txt").isFile()) return; // already exists

        File alternativeDirectory = new File(alternativePath);
        if (!alternativeDirectory.mkdirs()) return;
        try (PrintWriter writer = new PrintWriter(alternativePath + File.separator + "checked.txt", StandardCharsets.UTF_8)) {
            writer.println("# Please check out https://stopmodreposts.org/ for more information on why this feature exists.");
            writer.println("checked=true");
        } catch (Exception ignored) {}
    }

    @Deprecated
    private static List<String> getInstalledModFilenames() {

        List<String> INSTALLED_MOD_FILENAMES = new ArrayList<String>();
        
        File MOD_DIRECTORY = new File(Services.PLATFORM.getGameDirectory() + File.separator + "mods");
        File[] DISCOVERED_FILES = MOD_DIRECTORY.listFiles();
        File VERSIONED_MOD_DIRECTORY = new File(Services.PLATFORM.getGameDirectory() + File.separator + "mods" + File.separator + SharedConstants.VERSION_STRING);
        File[] DISCOVERED_VERSIONED_FILES = VERSIONED_MOD_DIRECTORY.listFiles();
        
        if (DISCOVERED_FILES == null && DISCOVERED_VERSIONED_FILES == null) return new ArrayList<String>();

        for (File file : ArrayUtils.addAll(DISCOVERED_FILES, DISCOVERED_VERSIONED_FILES)) {
            if (file.isFile()) {
                String filename = file.getName().replaceAll(" +\\([0-9]+\\)", "");
                INSTALLED_MOD_FILENAMES.add(filename);
            }
        }

        return INSTALLED_MOD_FILENAMES;
    }

    @Deprecated
    private static List<String> getModNamesMissingJarFile() {

        final List<String> INSTALLED_MOD_FILENAMES = getInstalledModFilenames();
        List<String> MOD_NAMES_MISSING_JAR_FILE = new ArrayList<String>();

        for (String filename : FILENAME_TO_MOD_NAME_MAP.keySet()) {

            final boolean CONTAINS_MERGED = INSTALLED_MOD_FILENAMES.contains(filename);
            final boolean CONTAINS_FABRIC = INSTALLED_MOD_FILENAMES.contains(filename.replace("-merged-", "-fabric-"));
            final boolean CONTAINS_FORGE = INSTALLED_MOD_FILENAMES.contains(filename.replace("-merged-", "-forge-"));
            final boolean CONTAINS_NEOFORGE = INSTALLED_MOD_FILENAMES.contains(filename.replace("-merged-", "-neoforge-"));

            if (!INSTALLED_MOD_FILENAMES.isEmpty() && !(CONTAINS_MERGED || CONTAINS_FABRIC || CONTAINS_FORGE || CONTAINS_NEOFORGE) && FILENAME_TO_MOD_NAME_MAP.containsKey(filename)) {
                MOD_NAMES_MISSING_JAR_FILE.add(FILENAME_TO_MOD_NAME_MAP.get(filename));
            }
        }

        if (!MOD_NAMES_MISSING_JAR_FILE.isEmpty()) Collections.sort(MOD_NAMES_MISSING_JAR_FILE);

        return MOD_NAMES_MISSING_JAR_FILE;
    }

    @Deprecated
    public static void sendMessage(Player player, String message, ChatFormatting colour, boolean insertNewLineBeforeMessage, String url) {

        if (message.isEmpty()) return;
        if (insertNewLineBeforeMessage) player.sendSystemMessage(Component.literal(""));

        MutableComponent mutableMessage = Component.literal(message);
        mutableMessage.withStyle(colour);
        
        if (message.contains("http") || (url != null && !url.isEmpty())) {
            if (url != null && url.isEmpty()) {
                for (String word : message.split(" ")) {
                    if (word.contains("http")) {
                        url = word;
                        break;
                    }
                }
            }

            if (url != null && !url.isEmpty()) {
                Style clickstyle = mutableMessage.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
                mutableMessage.withStyle(clickstyle);
            }
        }

        player.sendSystemMessage(mutableMessage);
    }

    @Deprecated
    public static void sendMessage(Player player, String message, ChatFormatting color, String url) {
        sendMessage(player, message, color, false, url);
    }

    @Deprecated
    public static void sendMessage(Player player, String message, ChatFormatting color) {
        sendMessage(player, message, color, null);
    }
}
