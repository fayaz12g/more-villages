import json
import sys

# --- ZEN PALETTE CONFIGURATION ---
# Format: "Original_Hex": "New_Zen_Hex"
# Note: Use 6-digit hex codes (#RRGGBB). The script handles the Alpha (AA) automatically.

PALETTE_MAP = {
    # === IRON BODY -> SAKURA WOOD/CERAMIC ===
    
    # Highlights (White/Cream)
    "#e5e3e3": "#fff9f0", 
    "#e2dbd6": "#fff9f0", 
    "#dcdcdc": "#fff9f0",
    "#d6d6d6": "#fff9f0",

    # Mid-Tones (Light Pink)
    "#dbcdc2": "#ffe9f0", 
    "#c5c5c5": "#f0b6b5", 
    "#c5cecd": "#f0b6b5", 
    "#d0d0d0": "#f0b6b5",

    # Shadow/Tarnish (Darker Pink/Rose)
    "#d2bcaa": "#ee98a1", 
    "#bebebe": "#ee98a1", 
    "#bdbeb4": "#ee98a1",
    "#bdb6ac": "#ee98a1", 
    "#b4b2ac": "#ee98a1",
    "#b4a59c": "#ee98a1",

    # Deep Rust/Shading (Deep Cherry/Reddish)
    "#d0b096": "#e77883", 
    "#cfb198": "#e77883", 
    "#c2a48d": "#e77883",
    "#b99983": "#c04b71", 
    "#acaaa4": "#c04b71",
    "#9c9c9c": "#c04b71", 
    "#ada19a": "#c04b71",
    "#9c9183": "#c04b71",

    # === JOINTS & DARK DETAILS -> PURPLES ===
    
    # Lighter Joints (Plum)
    "#957965": "#843970", 
    "#837168": "#843970", 
    "#9c9089": "#843970",
    "#a5a5a7": "#843970", 
    "#9d8f88": "#843970",

    # Darker Joints (Deep Purple)
    "#8d7c73": "#75325c", 
    "#81624c": "#75325c", 
    "#736d62": "#75325c",
    "#735c4e": "#75325c",

    # Deepest Shadows (Dark Maroon/Black)
    "#6d5d53": "#540419", 
    "#6d5e55": "#540419", 
    "#6c5d54": "#540419",
    "#574030": "#540419", 
    "#553a28": "#2e020d", 
    "#543a27": "#2e020d",
    "#543d2e": "#2e020d", 
    "#553e2f": "#2e020d", 
    "#543b2a": "#2e020d",
    "#1e000e": "#1a0005",

    # === VINES -> VIBRANT ZEN GREEN ===
    "#74a332": "#8ab357", 
    "#658932": "#8ab357",
    "#5c7e2c": "#5f7a3a", 

    # === FLOWERS ===
    "#ffd500": "#e15f87", # Yellow -> Hot Pink
    "#8d002a": "#ffffff", # Red -> White
}

def swap_colors(input_path, output_path):
    try:
        print(f"Reading {input_path}...")
        with open(input_path, "r") as f:
            data = json.load(f)

        new_data = {}
        count = 0

        for coord, hex_code in data.items():
            # hex_code is likely #RRGGBBAA (8 chars + #)
            # We want to check #RRGGBB against our map
            
            # Normalize to lowercase for matching
            hex_lower = hex_code.lower()
            
            # Extract Base Color (#RRGGBB) and Alpha (AA)
            if len(hex_lower) == 9: # #RRGGBBAA
                base_hex = hex_lower[:7]
                alpha = hex_lower[7:]
            else: # #RRGGBB
                base_hex = hex_lower
                alpha = "ff" # Assume full opacity if missing

            # Check if this color exists in our map
            if base_hex in PALETTE_MAP:
                new_base = PALETTE_MAP[base_hex]
                new_data[coord] = new_base + alpha
                count += 1
            else:
                # Keep original color if no match found
                new_data[coord] = hex_code

        print(f"Processed {len(data)} pixels.")
        print(f"Swapped {count} colors.")
        
        with open(output_path, "w") as f:
            json.dump(new_data, f, indent=4)
            
        print(f"Success! Created {output_path}")

    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("Usage: python json_palette_swapper.py plains.json zen_golem.json")
    else:
        swap_colors(sys.argv[1], sys.argv[2])