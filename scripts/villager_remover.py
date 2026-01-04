import os
import nbtlib
from pathlib import Path
import shutil
from datetime import datetime

def backup_file(file_path):
    """Create a backup of the file"""
    backup_dir = file_path.parent / "backup"
    backup_dir.mkdir(exist_ok=True)
    
    timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
    backup_path = backup_dir / f"{file_path.stem}_backup_{timestamp}{file_path.suffix}"
    
    shutil.copy2(file_path, backup_path)
    return backup_path

def remove_villagers_from_nbt(file_path, create_backup=True):
    """Remove all villager entities from an NBT file"""
    try:
        # Create backup if requested
        if create_backup:
            backup_path = backup_file(file_path)
            print(f"  📦 Backup created: {backup_path.name}")
        
        # Load the NBT file
        nbt_file = nbtlib.load(file_path)
        
        # Track changes
        villagers_removed = 0
        zombie_villagers_removed = 0
        
        # Process entities list
        if 'entities' in nbt_file:
            original_count = len(nbt_file['entities'])
            new_entities = nbtlib.List()
            
            for entity in nbt_file['entities']:
                if isinstance(entity, nbtlib.tag.Compound) and 'nbt' in entity:
                    entity_nbt = entity['nbt']
                    if isinstance(entity_nbt, nbtlib.tag.Compound) and 'id' in entity_nbt:
                        entity_id = str(entity_nbt['id'])
                        
                        # Skip villagers and zombie villagers
                        if entity_id == "minecraft:villager":
                            villagers_removed += 1
                            continue
                        elif entity_id == "minecraft:zombie_villager":
                            zombie_villagers_removed += 1
                            continue
                
                # Keep all other entities
                new_entities.append(entity)
            
            # Update entities list
            nbt_file['entities'] = new_entities
            
            # Show what was removed
            if villagers_removed > 0 or zombie_villagers_removed > 0:
                print(f"  🗑️  Removed: {villagers_removed} villagers, {zombie_villagers_removed} zombie villagers")
                nbt_file.save(file_path)
                return True
        
        return False
        
    except Exception as e:
        print(f"  ❌ Error processing {file_path}: {e}")
        return False

def main():
    # Configuration
    PATHS_TO_CHECK = [
        "./src/main/resources/data/more-villages/structure/village/badlands",
        "./src/main/resources/data/more-villages/structure/village/cherry",
    ]
    
    CREATE_BACKUP = True  # Set to False to skip backups
    
    print("=" * 60)
    print("VILLAGER REMOVAL SCRIPT")
    print("=" * 60)
    print(f"Backup enabled: {CREATE_BACKUP}")
    print()
    
    # Confirm
    print("⚠️  This will remove all villager entities from NBT files.")
    print("   Villagers will still spawn naturally when villages generate!")
    response = input("\nContinue? (y/n): ")
    if response.lower() != 'y':
        print("Cancelled.")
        return
    
    print("\n" + "-" * 60)
    
    total_files = 0
    total_modified = 0
    
    # Process each path
    for root_dir in PATHS_TO_CHECK:
        if not os.path.exists(root_dir):
            print(f"⚠️  Path does not exist: {root_dir}")
            continue
        
        print(f"\n📁 Searching in: {os.path.abspath(root_dir)}")
        
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
            print(f"Processing: {nbt_file.name}")
            if remove_villagers_from_nbt(nbt_file, CREATE_BACKUP):
                modified_count += 1
                total_modified += 1
                print(f"  ✅ Modified\n")
            else:
                print(f"  ➖ No villagers found\n")
        
        print(f"Modified {modified_count} files in this directory")
    
    print("\n" + "=" * 60)
    print(f"✅ COMPLETE!")
    print(f"   Modified {total_modified} out of {total_files} total files")
    if CREATE_BACKUP:
        print(f"   Backups saved in 'backup' folders")
    print("=" * 60)

if __name__ == "__main__":
    main()