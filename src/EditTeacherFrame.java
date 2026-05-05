import knižnica.Svet;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditTeacherFrame extends JFrame
{
    // Polia, na ktoré sa nastaví focus pri validačnej chybe.
    private enum FocusField
    {
        FIRST_NAME,
        LAST_NAME
    }

    // Rovnaký formulár sa používa aj na pridanie, aj na úpravu učiteľa.
    private final AddTeacherForm form = new AddTeacherForm();

    // Učiteľ, ktorého údaje sa upravujú.
    private final Teacher teacher;

    public EditTeacherFrame(Teacher teacher)
    {
        this.teacher = teacher;

        // Základné nastavenie okna.
        setTitle("Úprava záznamu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/icons/pozor-dozor.png")).getImage());
        setContentPane(form.getRootPanel());
        pack();
        setLocationRelativeTo(null);

        // V režime úpravy sa tlačidlá formulára premenia na uloženie a vymazanie.
        form.getLeaveButton().setText("Vymazať");
        form.getSaveButton().setText("Uložiť");

        // Zatvorenie formulára bez uloženia zmien.
        form.getCancelButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // Uloženie upravených údajov učiteľa.
        form.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveTeacherChanges();
            }
        });

        // Vymazanie učiteľa zo zoznamu.
        form.getLeaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTeacher();
            }
        });

        // Naplnenie formulára aktuálnymi údajmi učiteľa.
        fillForm();
    }

    private void deleteTeacher()
    {
        // Potvrdenie pred trvalým odstránením učiteľa.
        int confirm = Svet.otazka(
                "Naozaj chcete vymazať učiteľa:\n" + teacher.getFullName() + "?",
                "Vymazať učiteľa"
        );

        if(confirm != 0)
        {
            return;
        }

        // Vymazanie učiteľa z CSV podľa ID.
        TeacherRepository teacherRepository = new TeacherRepository();
        boolean deleted = teacherRepository.deleteTeacherById(teacher.getId());

        if(deleted)
        {
            // Po zmene zoznamu učiteľov už prípadný rozpracovaný rozpis nie je platný.
            Main.invalidatePendingDuties();

            Svet.sprava("Učiteľ bol vymazaný.", "Hotovo");
            dispose();
        }
        else
        {
            Svet.sprava("Nepodarilo sa vymazať učiteľa.", "Chyba");
        }
    }

    private void saveTeacherChanges()
    {
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

        // Vytvorenie upravenej verzie učiteľa s pôvodným ID.
        Teacher updatedTeacher = new Teacher(
                teacher.getId(),
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

        // Zachovanie štatistických údajov učiteľa.
        updatedTeacher.setDutyCount(teacher.getDutyCount());
        updatedTeacher.setLastDutyWeek(teacher.getLastDutyWeek());

        // Uloženie upraveného učiteľa do CSV.
        TeacherRepository teacherRepository = new TeacherRepository();
        boolean updated = teacherRepository.updateTeacher(updatedTeacher);

        if(updated)
        {
            // Po zmene údajov učiteľov už prípadný rozpracovaný rozpis nie je platný.
            Main.invalidatePendingDuties();

            Svet.sprava("Upravený učiteľ: " + updatedTeacher.getFullName() + ", id: " + updatedTeacher.getId() + ".", "Pridaný učiteľ");
            dispose();
        }
        else
        {
            Svet.sprava("Nepodarilo sa uložiť učiteľa", "CHYBA");
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
                    case FIRST_NAME:
                        form.getFirstNameField().requestFocusInWindow();
                        break;
                    case LAST_NAME:
                        form.getLastNameField().requestFocusInWindow();
                }
            }
        });
    }

    private void fillForm()
    {
        // Predvyplnenie textových údajov učiteľa.
        form.setTitleBeforeText(teacher.getDegreeBeforeName());
        form.setFirstNameText(teacher.getFirstName());
        form.setMiddleNameText(teacher.getMiddleName());
        form.setLastNameText(teacher.getLastName());
        form.setTitleAfterText(teacher.getDegreeAfterName());

        // Predvyplnenie dostupnosti učiteľa.
        form.setMondaySelected(teacher.isCanMonday());
        form.setTuesdaySelected(teacher.isCanTuesday());
        form.setWednesdaySelected(teacher.isCanWednesday());
        form.setThursdaySelected(teacher.isCanThursday());
        form.setFridaySelected(teacher.isCanFriday());
    }
}