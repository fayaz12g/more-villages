import sys
import json
from PIL import Image

def json_to_image(input_path, output_path):
    try:
        # Load JSON data
        with open(input_path, "r") as f:
            data = json.load(f)

        # Create a new 128x128 transparent image
        img = Image.new("RGBA", (128, 128))
        pixels = img.load()

        for coord, hex_code in data.items():
            x, y = map(int, coord.split(","))
            
            # Parse Hex #RRGGBBAA
            hex_code = hex_code.lstrip("#")
            if len(hex_code) == 8:
                r = int(hex_code[0:2], 16)
                g = int(hex_code[2:4], 16)
                b = int(hex_code[4:6], 16)
                a = int(hex_code[6:8], 16)
                pixels[x, y] = (r, g, b, a)
            else:
                # Fallback for RGB #RRGGBB (assumes full opacity)
                r = int(hex_code[0:2], 16)
                g = int(hex_code[2:4], 16)
                b = int(hex_code[4:6], 16)
                pixels[x, y] = (r, g, b, 255)

        # Save image
        img.save(output_path)
        print(f"Success! Reconstructed image saved to {output_path}")

    except Exception as e:
        print(f"Error: {e}")

# Usage: python json_to_img.py input.json output.png
if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("Usage: python json_to_img.py <input_json> <output_image>")
    else:
        json_to_image(sys.argv[1], sys.argv[2])