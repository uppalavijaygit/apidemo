#!/usr/bin/env python3
"""
Convert Markdown to HTML (can be printed to PDF from browser)
"""
import sys
import os

try:
    from markdown_it import MarkdownIt
    from markdown_it.renderer import RendererHTML
    
    md = MarkdownIt("commonmark").enable(["table", "strikethrough"])
    
    def markdown_to_html(md_file, html_file):
        """Convert Markdown file to HTML"""
        # Read markdown file
        with open(md_file, 'r', encoding='utf-8') as f:
            md_content = f.read()
        
        # Convert markdown to HTML
        html_content = md.render(md_content)
        
        # Add CSS styling for PDF printing
        html_with_style = f"""<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reminder API - Requirements Document</title>
    <style>
        @media print {{
            @page {{
                size: A4;
                margin: 2cm;
            }}
            body {{
                margin: 0;
                padding: 0;
            }}
        }}
        
        body {{
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            max-width: 900px;
            margin: 0 auto;
            padding: 40px 20px;
            line-height: 1.7;
            color: #333;
            background-color: #fff;
        }}
        
        h1 {{
            color: #2c3e50;
            border-bottom: 4px solid #3498db;
            padding-bottom: 15px;
            margin-bottom: 30px;
            font-size: 2.5em;
        }}
        
        h2 {{
            color: #34495e;
            border-bottom: 2px solid #95a5a6;
            padding-bottom: 10px;
            margin-top: 40px;
            margin-bottom: 20px;
            font-size: 1.8em;
        }}
        
        h3 {{
            color: #555;
            margin-top: 30px;
            margin-bottom: 15px;
            font-size: 1.4em;
        }}
        
        h4 {{
            color: #666;
            margin-top: 20px;
            margin-bottom: 10px;
            font-size: 1.2em;
        }}
        
        table {{
            border-collapse: collapse;
            width: 100%;
            margin: 25px 0;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }}
        
        th {{
            background-color: #3498db;
            color: white;
            font-weight: bold;
            padding: 15px;
            text-align: left;
            border: 1px solid #2980b9;
        }}
        
        td {{
            padding: 12px 15px;
            border: 1px solid #ddd;
        }}
        
        tr:nth-child(even) {{
            background-color: #f8f9fa;
        }}
        
        tr:hover {{
            background-color: #e8f4f8;
        }}
        
        code {{
            background-color: #f4f4f4;
            padding: 3px 8px;
            border-radius: 4px;
            font-family: 'Courier New', Consolas, monospace;
            font-size: 0.9em;
            color: #e83e8c;
        }}
        
        pre {{
            background-color: #f4f4f4;
            padding: 20px;
            border-radius: 6px;
            overflow-x: auto;
            border-left: 4px solid #3498db;
            margin: 20px 0;
        }}
        
        pre code {{
            background-color: transparent;
            padding: 0;
            color: #333;
        }}
        
        blockquote {{
            border-left: 4px solid #3498db;
            margin: 25px 0;
            padding: 15px 25px;
            background-color: #f8f9fa;
            color: #555;
            font-style: italic;
        }}
        
        ul, ol {{
            margin: 15px 0;
            padding-left: 40px;
        }}
        
        li {{
            margin: 8px 0;
        }}
        
        a {{
            color: #3498db;
            text-decoration: none;
        }}
        
        a:hover {{
            text-decoration: underline;
        }}
        
        hr {{
            border: none;
            border-top: 2px solid #ecf0f1;
            margin: 40px 0;
        }}
        
        .toc {{
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 6px;
            margin: 30px 0;
        }}
        
        .toc ul {{
            list-style-type: none;
            padding-left: 20px;
        }}
        
        .toc a {{
            color: #34495e;
        }}
        
        .highlight-box {{
            background-color: #e8f4f8;
            border-left: 4px solid #3498db;
            padding: 15px 20px;
            margin: 20px 0;
            border-radius: 4px;
        }}
        
        @media print {{
            body {{
                padding: 0;
            }}
            h1, h2, h3 {{
                page-break-after: avoid;
            }}
            table {{
                page-break-inside: avoid;
            }}
        }}
    </style>
</head>
<body>
    {html_content}
    
    <hr>
    <footer style="text-align: center; color: #7f8c8d; margin-top: 50px; font-size: 0.9em;">
        <p>Reminder API Requirements Document - Version 1.0</p>
        <p>Generated on: {os.popen('date').read().strip()}</p>
    </footer>
</body>
</html>"""
        
        with open(html_file, 'w', encoding='utf-8') as f:
            f.write(html_with_style)
        
        print(f"✓ Successfully converted {md_file} to {html_file}")
        print("\n" + "="*60)
        print("To convert to PDF:")
        print("="*60)
        print("1. Open the HTML file in your web browser")
        print(f"   File: {html_file}")
        print("2. Press Ctrl+P (Windows/Linux) or Cmd+P (Mac)")
        print("3. Select 'Save as PDF' or 'Microsoft Print to PDF'")
        print("4. Click 'Save'")
        print("\nAlternatively, you can use online converters:")
        print("- https://www.ilovepdf.com/html-to-pdf")
        print("- https://www.adobe.com/acrobat/online/html-to-pdf.html")
        
except ImportError:
    print("Error: markdown-it-py library not found.")
    print("Please install: pip3 install markdown-it-py")
    sys.exit(1)

if __name__ == "__main__":
    md_file = sys.argv[1] if len(sys.argv) > 1 else "API_REQUIREMENTS_DOCUMENT.md"
    html_file = md_file.replace('.md', '.html')
    
    if not os.path.exists(md_file):
        print(f"Error: File {md_file} not found!")
        sys.exit(1)
    
    markdown_to_html(md_file, html_file)

