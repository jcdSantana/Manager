import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

/**
 * 
 * 
 * @author Juan Santana
 * @author Giulia Mendes
 */
public class CheckBoxPanel {

    /**
     * 
     * @param tagManager
     * @param gameManager
     * @param gameManagerGUI
     * @return
     */
    public static JScrollPane tagFilterPanel(TagManager tagManager, GameManager gameManager, GameTable gt, Color backgroundColor) {
        List<JCheckBox> tagsCheckBoxList = checkBoxList(tagManager, backgroundColor);
        filterCheckBox(tagsCheckBoxList, tagManager, gameManager, gt);
        JLabel tagTitle = new JLabel("Tags");
        JPanel tagsPanel = new JPanel();
        tagsPanel.setLayout(new BoxLayout(tagsPanel, BoxLayout.Y_AXIS));
        tagsPanel.setBackground(backgroundColor);
        tagsPanel.add(tagTitle);
        for (JCheckBox tagCheckBox : tagsCheckBoxList)
            tagsPanel.add(tagCheckBox);
        JScrollPane tagsScrollPane = new JScrollPane(tagsPanel);
        tagsScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));

        return tagsScrollPane;
    }

    public static void removeTagFromPanel(JScrollPane tagFilterPanel, List<String> tagNameList) {
        JPanel tagsPanel = (JPanel) tagFilterPanel.getViewport().getView();
        Component[] components = tagsPanel.getComponents();
        List<Component> toRemove = new ArrayList<>();
        
        for (Component c : components) {
            if (c instanceof JCheckBox) {
                for (String tagName : tagNameList) {
                    if (tagName.equals(((JCheckBox) c).getText())) {
                        toRemove.add(c);
                    }
                }
            }
        }
        
        for (Component c : toRemove) {
            tagsPanel.remove(c);
        }
        
        tagsPanel.revalidate();
        tagsPanel.repaint();
    }

    /**
     * 
     * @param tagManager
     * @return
     */
    public static List<JCheckBox> checkBoxList(TagManager tagManager, Color backgroundColor) {
        List<JCheckBox> tagsCheckBoxList = new ArrayList<JCheckBox>();
        if (tagManager.getTagList() != null) {
            for (Tag tag : tagManager.getTagList()) {
                JCheckBox tagCheckBox = new JCheckBox(tag.getTagName());
                tagCheckBox.setBackground(backgroundColor);
                tagsCheckBoxList.add(tagCheckBox);
            }
        }
        return tagsCheckBoxList;
    }

    /**
     * 
     * @param tagsCheckBoxList
     * @param tagManager
     * @param gameManager
     * @param gameManagerGUI
     */
    private static void filterCheckBox(List<JCheckBox> tagsCheckBoxList, TagManager tagManager, GameManager gameManager, GameTable gt) {
        for (JCheckBox tagCheckBox : tagsCheckBoxList) {
            tagCheckBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    List<Tag> tagList = new ArrayList<Tag>();
                    for (JCheckBox aux : tagsCheckBoxList) {
                        if (aux.isSelected()) {
                            Tag t = tagManager.tagDict.get(aux.getText());
                            if (t != null) {
                                tagList.add(t);
                            }
                        }
                    }
                    if (tagList.isEmpty())
                        gameManager.filterByTag(tagList, false);
                    gameManager.filterByTag(tagList, true);
                    gt.updateGameTable(gameManager);
                }
            });
        }
    }
}
