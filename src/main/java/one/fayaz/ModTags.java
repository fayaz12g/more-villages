//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package one.fayaz;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class ModTags {
    public static final TagKey<Biome> HAS_VILLAGE_JUNGLE = create("has_structure/village_jungle");
    public static final TagKey<Biome> HAS_VILLAGE_CHERRY = create("has_structure/village_cherry");
    public static final TagKey<Biome> HAS_VILLAGE_BADLANDS = create("has_structure/village_badlands");
    public static final TagKey<Biome> HAS_VILLAGE_BEACH = create("has_structure/village_beach");
    public static final TagKey<Biome> HAS_VILLAGE_SWAMP = create("has_structure/village_swamp");

    private ModTags() {
    }

    private static TagKey<Biome> create(final String name) {
        return TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(MoreVillages.MOD_ID, name));
    }
}
