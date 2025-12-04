#!/usr/bin/env python3
"""
Convert Markdown to PDF
"""
import sys
import os

try:
    import markdown
    from weasyprint import HTML, CSS
    from weasyprint.text.fonts import FontConfiguration
    
    def markdown_to_pdf(md_file, pdf_file):
        """Convert Markdown file to PDF"""
        # Read markdown file
        with open(md_file, 'r', encoding='utf-8') as f:
            md_content = f.read()
        
        # Convert markdown to HTML
        html_content = markdown.markdown(md_content, extensions=['extra', 'codehilite', 'tables'])
        
        # Add CSS styling
        html_with_style = f"""
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <style>
                @page {{
                    size: A4;
                    margin: 2cm;
                }}
                body {{
                    font-family: 'DejaVu Sans', Arial, sans-serif;
                    line-height: 1.6;
                    color: #333;
                }}
                h1 {{
                    color: #2c3e50;
                    border-bottom: 3px solid #3498db;
                    padding-bottom: 10px;
                }}
                h2 {{
                    color: #34495e;
                    border-bottom: 2px solid #95a5a6;
                    padding-bottom: 5px;
                    margin-top: 30px;
                }}
                h3 {{
                    color: #555;
                    margin-top: 20px;
                }}
                table {{
                    border-collapse: collapse;
                    width: 100%;
                    margin: 20px 0;
                }}
                th, td {{
                    border: 1px solid #ddd;
                    padding: 12px;
                    text-align: left;
                }}
                th {{
                    background-color: #3498db;
                    color: white;
                    font-weight: bold;
                }}
                tr:nth-child(even) {{
                    background-color: #f2f2f2;
                }}
                code {{
                    background-color: #f4f4f4;
                    padding: 2px 6px;
                    border-radius: 3px;
                    font-family: 'Courier New', monospace;
                }}
                pre {{
                    background-color: #f4f4f4;
                    padding: 15px;
                    border-radius: 5px;
                    overflow-x: auto;
                }}
                blockquote {{
                    border-left: 4px solid #3498db;
                    margin: 20px 0;
                    padding-left: 20px;
                    color: #666;
                }}
                ul, ol {{
                    margin: 10px 0;
                    padding-left: 30px;
                }}
                li {{
                    margin: 5px 0;
                }}
            </style>
        </head>
        <body>
            {html_content}
        </body>
        </html>
        """
        
        # Convert HTML to PDF
        HTML(string=html_with_style).write_pdf(pdf_file)
        print(f"✓ Successfully converted {md_file} to {pdf_file}")
        
except ImportError:
    # Fallback: Convert to HTML and provide instructions
    print("PDF conversion libraries not available.")
    print("Creating HTML version instead...")
    
    try:
        import markdown
        
        md_file = sys.argv[1] if len(sys.argv) > 1 else "API_REQUIREMENTS_DOCUMENT.md"
        html_file = md_file.replace('.md', '.html')
        
        with open(md_file, 'r', encoding='utf-8') as f:
            md_content = f.read()
        
        html_content = markdown.markdown(md_content, extensions=['extra', 'codehilite', 'tables'])
        
        html_with_style = f"""
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <title>Reminder API Requirements Document</title>
            <style>
                body {{
                    font-family: Arial, sans-serif;
                    max-width: 900px;
                    margin: 0 auto;
                    padding: 20px;
                    line-height: 1.6;
                }}
                h1 {{ color: #2c3e50; border-bottom: 3px solid #3498db; }}
                h2 {{ color: #34495e; border-bottom: 2px solid #95a5a6; margin-top: 30px; }}
                table {{ border-collapse: collapse; width: 100%; margin: 20px 0; }}
                th, td {{ border: 1px solid #ddd; padding: 12px; text-align: left; }}
                th {{ background-color: #3498db; color: white; }}
                code {{ background-color: #f4f4f4; padding: 2px 6px; }}
                pre {{ background-color: #f4f4f4; padding: 15px; overflow-x: auto; }}
            </style>
        </head>
        <body>
            {html_content}
        </body>
        </html>
        """
        
        with open(html_file, 'w', encoding='utf-8') as f:
            f.write(html_with_style)
        
        print(f"✓ Created HTML file: {html_file}")
        print("\nTo convert to PDF:")
        print("1. Open the HTML file in your browser")
        print("2. Press Ctrl+P (or Cmd+P on Mac)")
        print("3. Select 'Save as PDF' as the destination")
        print("4. Click Save")
        
    except ImportError:
        print("Error: markdown library not found.")
        print("Please install: pip3 install --user markdown")
        sys.exit(1)

if __name__ == "__main__":
    md_file = sys.argv[1] if len(sys.argv) > 1 else "API_REQUIREMENTS_DOCUMENT.md"
    pdf_file = md_file.replace('.md', '.pdf')
    
    if not os.path.exists(md_file):
        print(f"Error: File {md_file} not found!")
        sys.exit(1)
    
    markdown_to_pdf(md_file, pdf_file)

