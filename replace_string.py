import os
from pathlib import Path

def replace_in_file(file_path, old_string, new_string):
    """Replace string in a single file"""
    try:
        # Try to read as text
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # Check if the old string exists
        if old_string not in content:
            return False
        
        # Count occurrences
        count = content.count(old_string)
        
        # Replace
        new_content = content.replace(old_string, new_string)
        
        # Write back
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(new_content)
        
        print(f"  ✓ Replaced {count} occurrence(s)")
        return True
        
    except UnicodeDecodeError:
        # Skip binary files
        print(f"  ⊘ Skipped (binary file)")
        return False
    except Exception as e:
        print(f"  ✗ Error: {e}")
        return False

def main():
    # Configuration
    OLD_STRING = "repurposed_structures"
    NEW_STRING = "more-villages"
    ROOT_DIR = "./src/main/resources/data/more-villages"
    
    # File extensions to process (empty list = all files)
    FILE_EXTENSIONS = ['.json', '.txt', '.mcmeta', '.nbt']  # Add more as needed
    # Or set to [] to process ALL files
    
    print("=" * 60)
    print("STRING REPLACEMENT SCRIPT")
    print("=" * 60)
    print(f"Directory: {os.path.abspath(ROOT_DIR)}")
    print(f"Find:      '{OLD_STRING}'")
    print(f"Replace:   '{NEW_STRING}'")
    if FILE_EXTENSIONS:
        print(f"File types: {', '.join(FILE_EXTENSIONS)}")
    else:
        print("File types: ALL")
    print("-" * 60)
    
    # Confirm
    response = input("\nContinue? (y/n): ")
    if response.lower() != 'y':
        print("Cancelled.")
        return
    
    print()
    
    # Find all files
    all_files = []
    for file_path in Path(ROOT_DIR).rglob("*"):
        if file_path.is_file():
            # Filter by extension if specified
            if not FILE_EXTENSIONS or file_path.suffix in FILE_EXTENSIONS:
                all_files.append(file_path)
    
    if not all_files:
        print("No files found!")
        return
    
    print(f"Found {len(all_files)} files to check\n")
    print("-" * 60)
    
    # Process each file
    modified_count = 0
    for file_path in all_files:
        # Show relative path
        rel_path = file_path.relative_to(ROOT_DIR)
        print(f"\n{rel_path}")
        
        if replace_in_file(file_path, OLD_STRING, NEW_STRING):
            modified_count += 1
    
    print("\n" + "=" * 60)
    print(f"✅ COMPLETE!")
    print(f"   Modified {modified_count} out of {len(all_files)} files")
    print("=" * 60)

if __name__ == "__main__":
    main()