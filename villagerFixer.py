import os
import nbtlib
from pathlib import Path

def fix_villager_types_in_nbt(file_path, type_replacements):
    """Fix VillagerData types in an NBT file"""
    try:
        # Load the NBT file
        nbt_file = nbtlib.load(file_path)
        
        # Track if any changes were made
        changed = False
        
        # Recursive function to process NBT data
        def process_nbt_data(data, parent_key=None):
            nonlocal changed
            
            if isinstance(data, nbtlib.tag.Compound):
                # Check if this is a VillagerData compound
                if 'type' in data and parent_key == 'VillagerData':
                    old_type = str(data['type'])
                    
                    # Check each replacement
                    for old_villager_type, new_villager_type in type_replacements.items():
                        if old_villager_type in old_type:
                            data['type'] = nbtlib.String(new_villager_type)
                            changed = True
                            print(f"  Fixed VillagerData type: {old_type} -> {new_villager_type}")
                            break
                
                # Continue recursively processing
                for key, value in data.items():
                    process_nbt_data(value, parent_key=key)
                    
            elif isinstance(data, nbtlib.tag.List):
                for item in data:
                    process_nbt_data(item)
        
        # Process the NBT data
        process_nbt_data(nbt_file)
        
        # Save if changes were made
        if changed:
            nbt_file.save(file_path)
            return True
        return False
        
    except Exception as e:
        print(f"Error processing {file_path}: {e}")
        return False

def main():
    # Configuration
    TYPE_REPLACEMENTS = {
        "minecraft:desert": "more-villages:badlands",
        "minecraft:plains": "more-villages:cherry",
    }
    
    # Paths to check
    PATHS_TO_CHECK = [
        "./src/main/resources/data/more-villages/structure/village/badlands",
        "./src/main/resources/data/more-villages/structure/village/cherry",
    ]
    
    print("VillagerData Type Replacements:")
    for old, new in TYPE_REPLACEMENTS.items():
        print(f"  {old} -> {new}")
    print("-" * 60)
    
    total_files = 0
    total_modified = 0
    
    # Process each path
    for root_dir in PATHS_TO_CHECK:
        if not os.path.exists(root_dir):
            print(f"⚠ Path does not exist: {root_dir}")
            continue
            
        print(f"\nSearching in: {os.path.abspath(root_dir)}")
        
        # Find all NBT files
        nbt_files = list(Path(root_dir).rglob("*.nbt"))
        
        if not nbt_files:
            print("  No NBT files found!")
            continue
        
        print(f"  Found {len(nbt_files)} NBT files\n")
        total_files += len(nbt_files)
        
        # Process each file
        modified_count = 0
        for nbt_file in nbt_files:
            print(f"  Processing: {nbt_file.name}")
            if fix_villager_types_in_nbt(nbt_file, TYPE_REPLACEMENTS):
                modified_count += 1
                total_modified += 1
                print(f"    ✓ Modified")
            else:
                print(f"    - No villagers found or no changes needed")
        
        print(f"\n  Modified {modified_count} files in this directory")
    
    print("\n" + "=" * 60)
    print(f"COMPLETE! Modified {total_modified} out of {total_files} total files")

if __name__ == "__main__":
    main()