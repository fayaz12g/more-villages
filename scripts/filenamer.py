import os
from pathlib import Path
import shutil

def rename_files(root_dir, old_name, new_name):
    """Rename all files containing old_name to new_name"""
    
    # Find all files with old_name in the filename
    all_files = list(Path(root_dir).rglob(f"*{old_name}*"))
    
    if not all_files:
        print(f"No files found with '{old_name}' in the name!")
        return
    
    print(f"Found {len(all_files)} files to rename\n")
    
    renamed_count = 0
    
    for file_path in all_files:
        # Get the new filename
        old_filename = file_path.name
        new_filename = old_filename.replace(old_name, new_name)
        
        # Get the full new path
        new_path = file_path.parent / new_filename
        
        # Check if target already exists
        if new_path.exists():
            print(f"⚠ Skipping {file_path}")
            print(f"  Target already exists: {new_path}\n")
            continue
        
        # Rename the file
        try:
            file_path.rename(new_path)
            print(f"✓ Renamed:")
            print(f"  From: {file_path}")
            print(f"  To:   {new_path}\n")
            renamed_count += 1
        except Exception as e:
            print(f"✗ Error renaming {file_path}: {e}\n")
    
    print("-" * 60)
    print(f"Complete! Renamed {renamed_count} out of {len(all_files)} files")

def main():
    # Configuration
    OLD_NAME = "desert"
    NEW_NAME = "badlands"
    ROOT_DIR = "./src/main/resources/data/more-villages/structure/village/badlands"
    
    print(f"Searching in: {os.path.abspath(ROOT_DIR)}")
    print(f"Renaming files: '{OLD_NAME}' -> '{NEW_NAME}'")
    print("-" * 60)
    print()
    
    # Confirm before proceeding
    response = input("This will rename files. Continue? (y/n): ")
    if response.lower() != 'y':
        print("Cancelled.")
        return
    
    print()
    rename_files(ROOT_DIR, OLD_NAME, NEW_NAME)

if __name__ == "__main__":
    main()