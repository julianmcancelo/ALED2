package beltran;

import javax.swing.text.*;

public class DNIFormatter extends DocumentFilter {

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
        String newText = currentText.substring(0, offset) + string + currentText.substring(offset);
        String formattedText = getFormattedString(newText);
        fb.replace(0, fb.getDocument().getLength(), formattedText, attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
        String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
        String formattedText = getFormattedString(newText);
        fb.replace(0, fb.getDocument().getLength(), formattedText, attrs);
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
        String newText = currentText.substring(0, offset) + currentText.substring(offset + length);
        String formattedText = getFormattedString(newText);
        fb.replace(0, fb.getDocument().getLength(), formattedText, null);
    }

    private String getFormattedString(String text) {
        // Remove all non-numeric characters
        text = text.replaceAll("\\D", "");
        
        // Limit to 8 digits
        if (text.length() > 8) {
            text = text.substring(0, 8);
        }
        
        // Format the text as ##.###.###
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (i == 2) {
                formatted.append('.');
            } else if (i > 2 && (text.length() - i) % 3 == 0) {
                formatted.append('.');
            }
            formatted.append(text.charAt(i));
        }
        
        return formatted.toString();
    }
}
