import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public final class UIStyles
{
    private static final Color SOFT_BG = new Color(242, 242, 242);
    private static final Color SOFT_TEXT = new Color(45, 45, 45);
    private static final Color SOFT_BORDER = new Color(185, 185, 185);

    private static final Color PRIMARY_BG = new Color(234, 240, 248);
    private static final Color PRIMARY_TEXT = new Color(52, 73, 94);
    private static final Color PRIMARY_BORDER = new Color(176, 190, 205);

    private static final Color SUCCESS_BG = new Color(232, 242, 234);
    private static final Color SUCCESS_TEXT = new Color(56, 87, 35);
    private static final Color SUCCESS_BORDER = new Color(176, 196, 170);

    private static final Color DANGER_BG = new Color(246, 232, 232);
    private static final Color DANGER_TEXT = new Color(122, 61, 61);
    private static final Color DANGER_BORDER = new Color(210, 185, 185);

    private UIStyles()
    {
    }

    public static void styleSoftButton(JButton button)
    {
        styleButton(button, SOFT_BG, SOFT_TEXT, SOFT_BORDER);
    }

    public static void stylePrimaryButton(JButton button)
    {
        styleButton(button, PRIMARY_BG, PRIMARY_TEXT, PRIMARY_BORDER);
    }

    public static void styleSuccessButton(JButton button)
    {
        styleButton(button, SUCCESS_BG, SUCCESS_TEXT, SUCCESS_BORDER);
    }

    public static void styleDangerButton(JButton button)
    {
        styleButton(button, DANGER_BG, DANGER_TEXT, DANGER_BORDER);
    }

    private static void styleButton(JButton button, Color background, Color foreground, Color borderColor)
    {
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setFont(new Font("SansSerif", Font.PLAIN, 13));

        button.setBorder(
                new CompoundBorder(
                        new LineBorder(borderColor, 1, true),
                        new EmptyBorder(6, 14, 6, 14)
                )
        );
    }
}