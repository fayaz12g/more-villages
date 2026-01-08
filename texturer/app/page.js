"use client";
import React, { useState, useRef, useEffect } from 'react';
import { Upload, Copy, RotateCcw, ZoomIn, ZoomOut } from 'lucide-react';

export default function MinecraftTextureEditor() {
  const [image, setImage] = useState(null);
  const [imageData, setImageData] = useState(null);
  const [modifiedData, setModifiedData] = useState(null);
  const [originalSelectedData, setOriginalSelectedData] = useState(null);
  const [selectionMode, setSelectionMode] = useState('pixel');
  const [selectedPixels, setSelectedPixels] = useState(new Set());
  const [selectedColors, setSelectedColors] = useState(new Set());
  const [currentColor, setCurrentColor] = useState('#ffffff');
  const [originalColor, setOriginalColor] = useState(null);
  const [copiedTranslation, setCopiedTranslation] = useState(null);
  const [zoom, setZoom] = useState(8);
  
  const canvasRef = useRef(null);
  const hiddenCanvasRef = useRef(null);
  const fileInputRef = useRef(null);

  const handleFileUpload = (e) => {
    const file = e.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = (event) => {
      const img = new Image();
      img.onload = () => {
        setImage(img);
        const canvas = hiddenCanvasRef.current;
        canvas.width = img.width;
        canvas.height = img.height;
        const ctx = canvas.getContext('2d');
        ctx.drawImage(img, 0, 0);
        const data = ctx.getImageData(0, 0, img.width, img.height);
        setImageData(data);
        setModifiedData(new Uint8ClampedArray(data.data));
        setSelectedPixels(new Set());
        setSelectedColors(new Set());
        setOriginalSelectedData(null);
      };
      img.src = event.target.result;
    };
    reader.readAsDataURL(file);
  };

  const rgbToHex = (r, g, b) => {
    return '#' + [r, g, b].map(x => x.toString(16).padStart(2, '0')).join('');
  };

  const hexToRgb = (hex) => {
    const result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
    return result ? {
      r: parseInt(result[1], 16),
      g: parseInt(result[2], 16),
      b: parseInt(result[3], 16)
    } : null;
  };

  const getPixelColor = (x, y, data = modifiedData) => {
    if (!imageData) return null;
    const idx = (y * imageData.width + x) * 4;
    return rgbToHex(data[idx], data[idx + 1], data[idx + 2]);
  };

  const getPixelsOfColor = (color) => {
    const pixels = new Set();
    if (!imageData) return pixels;
    
    for (let y = 0; y < imageData.height; y++) {
      for (let x = 0; x < imageData.width; x++) {
        const pixelColor = getPixelColor(x, y);
        const idx = (y * imageData.width + x) * 4;
        const alpha = modifiedData[idx + 3];
        if (pixelColor === color && alpha > 0) {
          pixels.add(`${x},${y}`);
        }
      }
    }
    return pixels;
  };

  const storeOriginalSelection = (pixels) => {
    if (!imageData) return;
    const originalData = new Uint8ClampedArray(modifiedData);
    setOriginalSelectedData(originalData);
  };

  const handleCanvasClick = (e) => {
    if (!imageData) return;

    const canvas = canvasRef.current;
    const rect = canvas.getBoundingClientRect();
    const x = Math.floor((e.clientX - rect.left) / zoom);
    const y = Math.floor((e.clientY - rect.top) / zoom);

    if (x < 0 || x >= imageData.width || y < 0 || y >= imageData.height) return;

    const clickedColor = getPixelColor(x, y);
    const idx = (y * imageData.width + x) * 4;
    const alpha = modifiedData[idx + 3];
    
    if (alpha === 0) return;

    if (selectionMode === 'pixel') {
      const newSelection = new Set([`${x},${y}`]);
      setSelectedPixels(newSelection);
      setSelectedColors(new Set([clickedColor]));
      setOriginalColor(clickedColor);
      setCurrentColor(clickedColor);
      storeOriginalSelection(newSelection);
    } else if (selectionMode === 'color') {
      const pixels = getPixelsOfColor(clickedColor);
      setSelectedPixels(pixels);
      setSelectedColors(new Set([clickedColor]));
      setOriginalColor(clickedColor);
      setCurrentColor(clickedColor);
      storeOriginalSelection(pixels);
    } else if (selectionMode === 'multiple') {
      const pixels = getPixelsOfColor(clickedColor);
      const newSelection = new Set([...selectedPixels, ...pixels]);
      setSelectedPixels(newSelection);
      setSelectedColors(prev => new Set([...prev, clickedColor]));
      if (!originalColor) {
        setOriginalColor(clickedColor);
        setCurrentColor(clickedColor);
      }
      storeOriginalSelection(newSelection);
    } else if (selectionMode === 'exclude') {
      if (selectedColors.has(clickedColor)) {
        // If clicking a selected color, just update the reference color
        setOriginalColor(clickedColor);
        setCurrentColor(clickedColor);
      } else {
        // Exclude this color
        const pixelsToRemove = getPixelsOfColor(clickedColor);
        const newSelection = new Set(selectedPixels);
        pixelsToRemove.forEach(p => newSelection.delete(p));
        setSelectedPixels(newSelection);
        setSelectedColors(prev => {
          const newSet = new Set(prev);
          newSet.delete(clickedColor);
          return newSet;
        });
        storeOriginalSelection(newSelection);
      }
    }
  };

  const applyColorChange = (newColor) => {
    if (!imageData || selectedPixels.size === 0 || !originalSelectedData) return;

    const newData = new Uint8ClampedArray(modifiedData);
    const newRgb = hexToRgb(newColor);
    const originalRgb = hexToRgb(originalColor);
    
    if (!originalRgb || !newRgb) return;

    const deltaR = newRgb.r - originalRgb.r;
    const deltaG = newRgb.g - originalRgb.g;
    const deltaB = newRgb.b - originalRgb.b;

    // Apply translation to all selected pixels based on their original colors
    selectedPixels.forEach(pixelKey => {
      const [x, y] = pixelKey.split(',').map(Number);
      const idx = (y * imageData.width + x) * 4;
      
      newData[idx] = Math.max(0, Math.min(255, originalSelectedData[idx] + deltaR));
      newData[idx + 1] = Math.max(0, Math.min(255, originalSelectedData[idx + 1] + deltaG));
      newData[idx + 2] = Math.max(0, Math.min(255, originalSelectedData[idx + 2] + deltaB));
    });

    setModifiedData(newData);
  };

  const handleColorChange = (e) => {
    const newColor = e.target.value;
    setCurrentColor(newColor);
    applyColorChange(newColor);
  };

  const copyTranslation = () => {
    if (!originalColor) return;
    
    const newRgb = hexToRgb(currentColor);
    const originalRgb = hexToRgb(originalColor);
    
    setCopiedTranslation({
      deltaR: newRgb.r - originalRgb.r,
      deltaG: newRgb.g - originalRgb.g,
      deltaB: newRgb.b - originalRgb.b
    });
  };

  const pasteTranslation = () => {
    if (!copiedTranslation || selectedPixels.size === 0 || !originalSelectedData) return;

    const newData = new Uint8ClampedArray(modifiedData);

    selectedPixels.forEach(pixelKey => {
      const [x, y] = pixelKey.split(',').map(Number);
      const idx = (y * imageData.width + x) * 4;
      
      newData[idx] = Math.max(0, Math.min(255, originalSelectedData[idx] + copiedTranslation.deltaR));
      newData[idx + 1] = Math.max(0, Math.min(255, originalSelectedData[idx + 1] + copiedTranslation.deltaG));
      newData[idx + 2] = Math.max(0, Math.min(255, originalSelectedData[idx + 2] + copiedTranslation.deltaB));
    });

    setModifiedData(newData);
  };

  const resetColors = () => {
    if (!imageData || selectedPixels.size === 0) return;

    const newData = new Uint8ClampedArray(modifiedData);

    selectedPixels.forEach(pixelKey => {
      const [x, y] = pixelKey.split(',').map(Number);
      const idx = (y * imageData.width + x) * 4;
      
      newData[idx] = imageData.data[idx];
      newData[idx + 1] = imageData.data[idx + 1];
      newData[idx + 2] = imageData.data[idx + 2];
    });

    setModifiedData(newData);
    setSelectedPixels(new Set());
    setSelectedColors(new Set());
    setOriginalSelectedData(null);
  };

  const downloadImage = () => {
    if (!imageData) return;

    const canvas = hiddenCanvasRef.current;
    const ctx = canvas.getContext('2d');
    const newImageData = new ImageData(modifiedData, imageData.width, imageData.height);
    ctx.putImageData(newImageData, 0, 0);
    
    canvas.toBlob(blob => {
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'modified-texture.png';
      a.click();
      URL.revokeObjectURL(url);
    });
  };

  useEffect(() => {
    if (!imageData || !canvasRef.current) return;

    const canvas = canvasRef.current;
    canvas.width = imageData.width * zoom;
    canvas.height = imageData.height * zoom;
    const ctx = canvas.getContext('2d');
    
    const tempCanvas = document.createElement('canvas');
    tempCanvas.width = imageData.width;
    tempCanvas.height = imageData.height;
    const tempCtx = tempCanvas.getContext('2d');
    const newImageData = new ImageData(modifiedData, imageData.width, imageData.height);
    tempCtx.putImageData(newImageData, 0, 0);
    
    ctx.imageSmoothingEnabled = false;
    ctx.drawImage(tempCanvas, 0, 0, imageData.width * zoom, imageData.height * zoom);

    ctx.strokeStyle = '#ffff00';
    ctx.lineWidth = 2;
    selectedPixels.forEach(pixelKey => {
      const [x, y] = pixelKey.split(',').map(Number);
      ctx.strokeRect(x * zoom, y * zoom, zoom, zoom);
    });
  }, [imageData, modifiedData, selectedPixels, zoom]);

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-900 to-gray-800 p-8">
      <div className="max-w-7xl mx-auto">
        <div className="bg-gray-800 rounded-lg shadow-2xl p-6 mb-6">
          <h1 className="text-3xl font-bold text-white mb-2">Minecraft Texture Editor</h1>
          <p className="text-gray-400 mb-4">Upload and edit small textures with advanced color selection tools</p>
          
          <div className="flex flex-wrap gap-3 mb-6">
            <button
              onClick={() => fileInputRef.current?.click()}
              className="flex items-center gap-2 px-4 py-2 bg-green-600 hover:bg-green-700 text-white rounded-lg transition"
            >
              <Upload size={20} />
              Upload Texture
            </button>
            <input
              ref={fileInputRef}
              type="file"
              accept="image/*"
              onChange={handleFileUpload}
              className="hidden"
            />
            
            {image && (
              <>
                <button
                  onClick={downloadImage}
                  className="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg transition"
                >
                  Download
                </button>
                
                <button
                  onClick={copyTranslation}
                  className="flex items-center gap-2 px-4 py-2 bg-purple-600 hover:bg-purple-700 text-white rounded-lg transition disabled:opacity-50"
                  disabled={selectedPixels.size === 0}
                >
                  <Copy size={20} />
                  Copy Translation
                </button>
                
                <button
                  onClick={pasteTranslation}
                  className="px-4 py-2 bg-purple-600 hover:bg-purple-700 text-white rounded-lg transition disabled:opacity-50"
                  disabled={!copiedTranslation || selectedPixels.size === 0}
                >
                  Paste Translation
                </button>
                
                <button
                  onClick={resetColors}
                  className="flex items-center gap-2 px-4 py-2 bg-red-600 hover:bg-red-700 text-white rounded-lg transition disabled:opacity-50"
                  disabled={selectedPixels.size === 0}
                >
                  <RotateCcw size={20} />
                  Reset Colors
                </button>

                <div className="flex items-center gap-2 ml-auto">
                  <button
                    onClick={() => setZoom(Math.max(2, zoom - 2))}
                    className="p-2 bg-gray-700 hover:bg-gray-600 text-white rounded-lg transition"
                  >
                    <ZoomOut size={20} />
                  </button>
                  <span className="text-white font-mono">{zoom}x</span>
                  <button
                    onClick={() => setZoom(Math.min(20, zoom + 2))}
                    className="p-2 bg-gray-700 hover:bg-gray-600 text-white rounded-lg transition"
                  >
                    <ZoomIn size={20} />
                  </button>
                </div>
              </>
            )}
          </div>

          {image && (
            <div className="mb-6">
              <h3 className="text-white font-semibold mb-3">Selection Mode:</h3>
              <div className="flex flex-wrap gap-2">
                <button
                  onClick={() => {
                    setSelectionMode('pixel');
                    setSelectedPixels(new Set());
                    setSelectedColors(new Set());
                    setOriginalSelectedData(null);
                  }}
                  className={`px-4 py-2 rounded-lg transition ${
                    selectionMode === 'pixel'
                      ? 'bg-blue-600 text-white'
                      : 'bg-gray-700 text-gray-300 hover:bg-gray-600'
                  }`}
                >
                  Select Pixel
                </button>
                <button
                  onClick={() => {
                    setSelectionMode('color');
                    setSelectedPixels(new Set());
                    setSelectedColors(new Set());
                    setOriginalSelectedData(null);
                  }}
                  className={`px-4 py-2 rounded-lg transition ${
                    selectionMode === 'color'
                      ? 'bg-blue-600 text-white'
                      : 'bg-gray-700 text-gray-300 hover:bg-gray-600'
                  }`}
                >
                  Select Color
                </button>
                <button
                  onClick={() => {
                    setSelectionMode('multiple');
                    setSelectedPixels(new Set());
                    setSelectedColors(new Set());
                    setOriginalSelectedData(null);
                  }}
                  className={`px-4 py-2 rounded-lg transition ${
                    selectionMode === 'multiple'
                      ? 'bg-blue-600 text-white'
                      : 'bg-gray-700 text-gray-300 hover:bg-gray-600'
                  }`}
                >
                  Select Multiple Colors
                </button>
                <button
                  onClick={() => {
                    setSelectionMode('exclude');
                    const allPixels = new Set();
                    const allColors = new Set();
                    for (let py = 0; py < imageData.height; py++) {
                      for (let px = 0; px < imageData.width; px++) {
                        const pidx = (py * imageData.width + px) * 4;
                        if (modifiedData[pidx + 3] > 0) {
                          allPixels.add(`${px},${py}`);
                          allColors.add(getPixelColor(px, py));
                        }
                      }
                    }
                    setSelectedPixels(allPixels);
                    setSelectedColors(allColors);
                    storeOriginalSelection(allPixels);
                  }}
                  className={`px-4 py-2 rounded-lg transition ${
                    selectionMode === 'exclude'
                      ? 'bg-blue-600 text-white'
                      : 'bg-gray-700 text-gray-300 hover:bg-gray-600'
                  }`}
                >
                  Exclude
                </button>
              </div>
              {selectionMode === 'exclude' && selectedPixels.size > 0 && (
                <p className="text-sm text-gray-400 mt-2">
                  Click colors to exclude them from selection. Click a selected color to use it as reference.
                </p>
              )}
            </div>
          )}
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
          <div className="lg:col-span-3">
            {image && (
              <div className="bg-gray-800 rounded-lg shadow-2xl p-6">
                <div className="overflow-auto max-h-[600px]">
                  <div className="inline-block bg-gray-700 p-4 rounded">
                    <canvas
                      ref={canvasRef}
                      onClick={handleCanvasClick}
                      className="cursor-crosshair border-2 border-gray-600"
                      style={{ imageRendering: 'pixelated' }}
                    />
                  </div>
                </div>
              </div>
            )}
          </div>

          {image && selectedPixels.size > 0 && (
            <div className="bg-gray-800 rounded-lg shadow-2xl p-6">
              <h3 className="text-white font-semibold mb-4">Color Adjustment</h3>
              
              <div className="space-y-4">
                <div>
                  <p className="text-gray-400 text-sm mb-2">Original Color</p>
                  <div className="flex items-center gap-3">
                    <div
                      className="w-16 h-16 rounded border-2 border-gray-600"
                      style={{ backgroundColor: originalColor }}
                    />
                    <div>
                      <p className="text-white font-mono text-sm">{originalColor?.toUpperCase()}</p>
                    </div>
                  </div>
                </div>

                <div>
                  <p className="text-gray-400 text-sm mb-2">New Color</p>
                  <div className="flex items-center gap-3 mb-3">
                    <div
                      className="w-16 h-16 rounded border-2 border-gray-600"
                      style={{ backgroundColor: currentColor }}
                    />
                    <div>
                      <p className="text-white font-mono text-sm">{currentColor.toUpperCase()}</p>
                    </div>
                  </div>
                  
                  <input
                    type="color"
                    value={currentColor}
                    onChange={handleColorChange}
                    className="w-full h-32 cursor-pointer rounded border-2 border-gray-600"
                  />
                </div>

                <div className="pt-4 border-t border-gray-700">
                  <p className="text-gray-400 text-xs">
                    {selectedPixels.size} pixel{selectedPixels.size !== 1 ? 's' : ''} selected
                  </p>
                  <p className="text-gray-400 text-xs mt-1">
                    {selectedColors.size} color{selectedColors.size !== 1 ? 's' : ''} in selection
                  </p>
                </div>
              </div>
            </div>
          )}
        </div>

        <canvas ref={hiddenCanvasRef} className="hidden" />
      </div>
    </div>
  );
}