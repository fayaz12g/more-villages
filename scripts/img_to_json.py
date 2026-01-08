import sys
import json
from PIL import Image

def image_to_json(input_path, output_path):
    try:
        # Open image
        img = Image.open(input_path).convert("RGBA")
        
        # Resize to 128x16 using Nearest Neighbor to preserve crisp pixel edges
        img = img.resize((128, 128), resample=Image.Resampling.NEAREST)
        
        pixels = img.load()
        data = {}

        # Iterate through every pixel (0 to 15)
        for y in range(128):
            for x in range(128):
                r, g, b, a = pixels[x, y]
                # Convert to Hex #RRGGBBAA
                hex_code = f"#{r:02x}{g:02x}{b:02x}{a:02x}"
                data[f"{x},{y}"] = hex_code

        # Save to JSON
        with open(output_path, "w") as f:
            json.dump(data, f, indent=4)
        
        print(f"Success! Converted {input_path} to {output_path}")

    except Exception as e:
        print(f"Error: {e}")

# Usage: python img_to_json.py input.png output.json
if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("Usage: python img_to_json.py <input_image> <output_json>")
    else:
        image_to_json(sys.argv[1], sys.argv[2])