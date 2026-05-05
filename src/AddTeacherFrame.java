import knižnica.Svet;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddTeacherFrame extends JFrame
{
    // Polia, na ktoré môže aplikácia presunúť kurzor pri chybe alebo po uložení.
    private enum FocusField
    {
        TITLE_BEFORE,
        FIRST_NAME,
        LAST_NAME
    }

    // Posledný vytvorený učiteľ.
    private Teacher newTeacher;

    // Repository pre prácu so súborom teachers.csv.
    private final TeacherRepository teacherRepository = new TeacherRepository();

    // Swing formulár vytvorený cez IntelliJ GUI Designer.
    private final AddTeacherForm form = new AddTeacherForm();

    public AddTeacherFrame()
    {
        // Základné nastavenie okna.
        setTitle("Pridať učiteľa");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/icons/pozor-dozor.png")).getImage());
        setContentPane(form.getRootPanel());
        pack();
        setLocationRelativeTo(null);

        // Zrušenie formulára bez uloženia.
        form.getCancelButton().addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                dispose();
            }
        });

        // Uloženie učiteľa a ponechanie okna otvoreného.
        form.getSaveButton().addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                createTeacher(false);
            }
        });

        // Uloženie učiteľa a následné zatvorenie okna.
        form.getLeaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createTeacher(true);
            }
        });

        // Enter vo formulári aktivuje tlačidlo uloženia.
        getRootPane().setDefaultButton(form.getSaveButton());
    }

    public void createTeacher(boolean leave)
    {
        // Získanie nového ID učiteľa.
        int newId = teacherRepository.getNextId();

        if(newId == -1)
        {
            Svet.sprava("Neznáma chyba pri prideľovaní ID", "CHYBA");
            return;
        }

        // Načítanie textových údajov z formulára.
        String degreeBeforeName = form.getTitleBeforeText();
        String firstName = form.getFirstNameText();
        String middleName = form.getMiddleNameText();
        String lastName = form.getLastNameText();
        String degreeAfterName = form.getTitleAfterText();

        // Načítanie dostupnosti učiteľa podľa dní v týždni.
        boolean canMonday = form.isMondaySelected();
        boolean canTuesday = form.isTuesdaySelected();
        boolean canWednesday = form.isWednesdaySelected();
        boolean canThursday = form.isThursdaySelected();
        boolean canFriday = form.isFridaySelected();

        // Meno je povinné.
        if(firstName.isEmpty())
        {
            Svet.sprava("Meno je povinné", "Chyba");
            focus(FocusField.FIRST_NAME);
            return;
        }

        // Priezvisko je povinné.
        if(lastName.isEmpty())
        {
            Svet.sprava("Priezvisko je povinné", "Chyba");
            focus(FocusField.LAST_NAME);
            return;
        }

        // Vytvorenie objektu nového učiteľa.
        newTeacher = new Teacher(
                newId,
                degreeBeforeName,
                firstName,
                middleName,
                lastName,
                degreeAfterName,
                canMonday,
                canTuesday,
                canWednesday,
                canThursday,
                canFriday
        );

        // Uloženie učiteľa do CSV.
        boolean saved = teacherRepository.saveTeacher(newTeacher);

        if(!saved)
        {
            Svet.sprava("Nepodarilo sa uložiť učiteľa", "CHYBA");
            return;
        }

        // Po zmene zoznamu učiteľov už prípadný starý náhľad rozpisu nie je platný.
        Main.invalidatePendingDuties();

        Svet.sprava("Pridaný učiteľ: " + newTeacher.getFullName() + ", id: " + newTeacher.getId() + ".", "Pridaný učiteľ");

        if(leave)
        {
            // Po uložení sa okno zatvorí.
            dispose();
        }
        else
        {
            // Formulár sa vyčistí a pripraví na zadanie ďalšieho učiteľa.
            form.clearForm();
            focus(FocusField.TITLE_BEFORE);
        }
    }

    private void focus(FocusField field)
    {
        // Prenesenie okna dopredu pred nastavením focusu.
        setState(JFrame.NORMAL);
        toFront();
        requestFocus();

        // Focus sa nastavuje až po dokončení aktuálnej Swing udalosti.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                switch(field)
                {
                    case TITLE_BEFORE:
                        form.getTitleBeforeField().requestFocusInWindow();
                        break;
                    case FIRST_NAME:
                        form.getFirstNameField().requestFocusInWindow();
                        break;
                    case LAST_NAME:
                        form.getLastNameField().requestFocusInWindow();
                }
            }
        });
    }

    public Teacher getNewTeacher()
    {
        // Vracia posledného vytvoreného učiteľa.
        return this.newTeacher;
    }
}