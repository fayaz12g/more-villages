import os
import nbtlib
from pathlib import Path

def replace_blocks_in_nbt(file_path, block_replacements):
    """Replace block types in an NBT file"""
    try:
        # Load the NBT file
        nbt_file = nbtlib.load(file_path)
        
        # Track if any changes were made
        changed = False
        
        # Recursive function to process NBT data
        def process_nbt_data(data):
            nonlocal changed
            
            if isinstance(data, nbtlib.tag.Compound):
                for key, value in data.items():
                    if isinstance(value, nbtlib.tag.String):
                        old_value = str(value)
                        new_value = old_value
                        
                        # Try each replacement
                        for old_block, new_block in block_replacements.items():
                            if old_block in new_value:
                                new_value = new_value.replace(old_block, new_block)
                        
                        # If value changed, update it
                        if new_value != old_value:
                            data[key] = nbtlib.String(new_value)
                            changed = True
                            print(f"  Changed: {old_value} -> {new_value}")
                    else:
                        process_nbt_data(value)
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
    BLOCK_REPLACEMENTS = {
        # Order matters! Do more specific replacements first
        "mossy_stone_bricks": "deepslate_bricks",
        "mossy_stone_brick_stairs": "deepslate_brick_stairs",
        "mossy_stone_brick_slab": "deepslate_brick_slab",
        "mossy_stone_brick_wall": "deepslate_brick_wall",
        "stone_bricks": "deepslate_bricks",
        "stone_brick_stairs": "deepslate_brick_stairs",
        "stone_brick_slab": "deepslate_brick_slab",
        "stone_brick_wall": "deepslate_brick_wall",
        "sandstone": "red_sandstone",
    }
    
    ROOT_DIR = "./src/main/resources/data/more-villages/structure/village/"
    ROOT_DIR = ROOT_DIR + "badlands"  # Update this line to the target village
    
    print(f"Searching for NBT files in: {os.path.abspath(ROOT_DIR)}")
    print(f"Block replacements:")
    for old, new in BLOCK_REPLACEMENTS.items():
        print(f"  {old} -> {new}")
    print("-" * 60)
    
    # Find all NBT files
    nbt_files = list(Path(ROOT_DIR).rglob("*.nbt"))
    
    if not nbt_files:
        print("No NBT files found!")
        return
    
    print(f"Found {len(nbt_files)} NBT files\n")
    
    # Process each file
    modified_count = 0
    for nbt_file in nbt_files:
        print(f"Processing: {nbt_file}")
        if replace_blocks_in_nbt(nbt_file, BLOCK_REPLACEMENTS):
            modified_count += 1
            print(f"  ✓ Modified")
        else:
            print(f"  - No changes needed")
        print()
    
    print("-" * 60)
    print(f"Complete! Modified {modified_count} out of {len(nbt_files)} files")

if __name__ == "__main__":
    main()