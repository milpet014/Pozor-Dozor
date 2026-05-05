import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ListTeachersFrame extends JFrame
{
    // Formulár so zoznamom učiteľov vytvorený cez IntelliJ GUI Designer.
    private final ListTeachersForm form = new ListTeachersForm();

    // Zoznam učiteľov, ktorí sa majú zobraziť.
    private final List<Teacher> teachers;

    // Aktuálna stránka zoznamu.
    private int currentPage = 0;

    // Počet učiteľov zobrazených na jednej stránke.
    private static final int PAGE_SIZE = 10;

    public ListTeachersFrame(List<Teacher> teachers)
    {
        this.teachers = teachers;

        // Základné nastavenie okna.
        setTitle("Upraviť záznam učiteľa");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/icons/pozor-dozor.png")).getImage());
        setContentPane(form.getRootPanel());
        pack();
        setLocationRelativeTo(null);

        // Zatvorenie okna zoznamu.
        form.getOkButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // Prechod na ďalšiu stránku zoznamu.
        form.getNextButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextPage();
            }
        });

        // Prechod na predchádzajúcu stránku zoznamu.
        form.getBackButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previousPage();
            }
        });

        // Každé tlačidlo učiteľa otvorí zodpovedajúci záznam na úpravu.
        for(byte i  = 0; i < PAGE_SIZE; i++)
        {
            final byte buttonIndex = i;
            form.getTeacherButton(buttonIndex).addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openTeacher(buttonIndex);
                }
            });
        }

        // Prvé načítanie aktuálnej stránky.
        refreshPage();
    }

    private void openTeacher(byte buttonIndex)
    {
        // Prepočet indexu tlačidla na index učiteľa v celom zozname.
        int teacherIndex = currentPage * PAGE_SIZE + buttonIndex;

        Teacher teacher = teachers.get(teacherIndex);

        // Otvorenie okna úpravy vybraného učiteľa.
        EditTeacherFrame frame = new EditTeacherFrame(teacher);
        frame.setVisible(true);

        // Zoznam sa zatvorí, aby sa po úprave nepoužívali staré údaje.
        dispose();
    }

    private void refreshPage()
    {
        // Vyčistenie zoznamu pred vykreslením aktuálnej stránky.
        form.clearTeacherList();

        int startIndex = currentPage * PAGE_SIZE;

        // Vyplnenie tlačidiel učiteľmi z aktuálnej stránky.
        for(byte i = 0; i < PAGE_SIZE; i++)
        {
            int teacherIndex = startIndex + i;

            if(teacherIndex < teachers.size())
            {
                Teacher teacher = teachers.get(teacherIndex);

                form.setTeacherLabelText(i, teacher.getFullName());
                form.setTeacherButtonEnabled(i, true);
            }
        }

        // Výpočet celkového počtu strán.
        int totalPages = (teachers.size() + PAGE_SIZE - 1) / PAGE_SIZE;

        if(totalPages == 0) totalPages = 1;

        // Zobrazenie informácie o aktuálnej stránke.
        form.setPageLabelText("Strana " + (currentPage + 1) + " / " + totalPages);

        // Povolenie alebo zakázanie navigačných tlačidiel.
        form.getBackButton().setEnabled(currentPage > 0);
        form.getNextButton().setEnabled((currentPage + 1) * PAGE_SIZE < teachers.size());
    }

    private void nextPage()
    {
        // Prechod na ďalšiu stránku, ak existuje.
        if((currentPage + 1) * PAGE_SIZE < teachers.size())
        {
            currentPage++;
            refreshPage();
        }
    }

    private void previousPage()
    {
        // Prechod na predchádzajúcu stránku, ak existuje.
        if(currentPage > 0)
        {
            currentPage--;
            refreshPage();
        }
    }
}