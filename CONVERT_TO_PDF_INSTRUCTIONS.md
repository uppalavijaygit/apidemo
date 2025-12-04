# How to Convert Requirements Document to PDF

## Method 1: Using Browser (Easiest - Recommended)

1. **Open the HTML file** in your web browser:
   ```bash
   # On Linux/Mac:
   xdg-open API_REQUIREMENTS_DOCUMENT.html
   # or
   open API_REQUIREMENTS_DOCUMENT.html
   
   # Or simply double-click the file
   ```

2. **Print to PDF:**
   - Press `Ctrl+P` (Windows/Linux) or `Cmd+P` (Mac)
   - Select **"Save as PDF"** or **"Microsoft Print to PDF"** as the destination
   - Click **"Save"**
   - Choose a location and filename (e.g., `API_REQUIREMENTS_DOCUMENT.pdf`)

## Method 2: Using Online Converter

1. Go to one of these online converters:
   - https://www.ilovepdf.com/html-to-pdf
   - https://www.adobe.com/acrobat/online/html-to-pdf.html
   - https://www.freeconvert.com/html-to-pdf

2. Upload `API_REQUIREMENTS_DOCUMENT.html`
3. Click "Convert" or "Download PDF"

## Method 3: Using Command Line (if tools are available)

### Option A: Using wkhtmltopdf
```bash
wkhtmltopdf API_REQUIREMENTS_DOCUMENT.html API_REQUIREMENTS_DOCUMENT.pdf
```

### Option B: Using pandoc
```bash
pandoc API_REQUIREMENTS_DOCUMENT.md -o API_REQUIREMENTS_DOCUMENT.pdf --pdf-engine=wkhtmltopdf
```

### Option C: Using Python script (if libraries installed)
```bash
python3 convert_to_pdf.py API_REQUIREMENTS_DOCUMENT.md
```

## Files Created

- ✅ `API_REQUIREMENTS_DOCUMENT.md` - Original Markdown file
- ✅ `API_REQUIREMENTS_DOCUMENT.html` - HTML version (ready for PDF conversion)
- 📄 `API_REQUIREMENTS_DOCUMENT.pdf` - Will be created after conversion

## Quick Start

The easiest way is **Method 1** - just open the HTML file and print to PDF!

```bash
# Open the HTML file
xdg-open API_REQUIREMENTS_DOCUMENT.html  # Linux
# or double-click the file in your file manager
```

Then press `Ctrl+P` and select "Save as PDF"!

