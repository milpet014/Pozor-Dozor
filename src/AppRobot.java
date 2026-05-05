import knižnica.GRobot;
import knižnica.Svet;
import knižnica.UdajeUdalosti;
import knižnica.Subor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.swing.SwingUtilities;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Desktop;
import java.net.URI;
import java.util.List;

public class AppRobot extends GRobot
{
    // Otvorené okná aplikácie. Uchovávajú sa preto, aby sa neotvárali viackrát naraz.
    private AddTeacherFrame addTeacherFrame;
    private ListTeachersFrame listTeachersFrame;
    private MonthSettingsFrame monthSettingsFrame;
    private SettingsFrame settingsFrame;

    public AppRobot()
    {
        // Samotný robot nie je viditeľný, používa sa len na obsluhu udalostí.
        skry();
    };

    @Override
    public void volbaTlacidla()
    {
        // Ukončenie aplikácie.
        if(UdajeUdalosti.tlacidlo() == Main.getCloseButton())
        {
            Svet.koniec();
        }

        // Vygenerovanie iba náhľadu rozpisu.
        if(UdajeUdalosti.tlacidlo() == Main.getGenerateButton())
        {
            Main.generateCurrentMonthPreview(true);
        }

        // Potvrdenie rozpisu a zápis do CSV.
        if(UdajeUdalosti.tlacidlo() == Main.getConfirmButton())
        {
            Main.confirmCurrentMonthDuties();
        }

        // Uloženie aktuálneho PDF na zvolené miesto.
        if(UdajeUdalosti.tlacidlo() == Main.getSaveButton())
        {
            savePdfAs();
        }
    }

    @Override
    public void volbaPolozkyPonuky()
    {
        // Otvorenie formulára na pridanie učiteľa.
        if(UdajeUdalosti.polozkaPonuky() == Main.getAddTeacher())
        {
            SwingUtilities.invokeLater(new Runnable()
            {
                @Override
                public void run() {
                    openAddTeacherFrame();
                }
            });
        }

        // Otvorenie zoznamu učiteľov na úpravu.
        if(UdajeUdalosti.polozkaPonuky() == Main.getEditTeacher())
        {
            SwingUtilities.invokeLater(new Runnable()
            {
                @Override
                public void run() {
                    TeacherRepository teacherRepository = new TeacherRepository();
                    List<Teacher> teachers = teacherRepository.loadTeachers();
                    openListTeacherFrame(teachers);
                }
            });
        }

        // Otvorenie kalendára dní zahrnutých do generovania.
        if(UdajeUdalosti.polozkaPonuky() == Main.getCalendar())
        {
            SwingUtilities.invokeLater(new Runnable()
            {
                @Override
                public void run()
                {
                    openMonthSettingsFrame();
                }
            });
        }

        // Otvorenie nastavení aplikácie a PDF.
        if(UdajeUdalosti.polozkaPonuky() == Main.getSettings())
        {
            SwingUtilities.invokeLater(new Runnable()
            {
                @Override
                public void run()
                {
                    openSettingsFrame();
                }
            });
        }

        // Cloudové položky sa spracujú len vtedy, keď je cloud povolený.
        if(Main.usingCloud)
        {
            if(UdajeUdalosti.polozkaPonuky() == Main.getSaveToCloud())
            {
                saveAppFolderToCloud();
            }

            if(UdajeUdalosti.polozkaPonuky() == Main.getLoadFromCloud())
            {
                loadAppFolderFromCloud();
            }
        }

        // Informácia o verzii aplikácie.
        if(UdajeUdalosti.polozkaPonuky() == Main.getHelp())
        {
            Svet.sprava("Verzia: Beta-1.0", "O programe");
        }

        // Otvorenie online dokumentácie po potvrdení používateľom.
        if(UdajeUdalosti.polozkaPonuky() == Main.getDocumentation())
        {
            int answer = Svet.otazka("Chcete otvoriť online dokumentáciu? \n \n" + "pozor-dozor.milpet.eu", "Dokumentácia");

            if(answer == 0)
            {
                openWebPage("https://pozor-dozor.milpet.eu/");
            }
        }
    }

    private void openWebPage(String url)
    {
        try
        {
            // Otvorenie URL v predvolenom prehliadači systému.
            Desktop desktop = Desktop.getDesktop();

            desktop.browse(new URI(url));
        }
        catch(Exception e)
        {
            Svet.sprava("Nepodarilo sa otvoriť webovú stránku:\n" + e.getMessage(), "Chyba");
        }
    }

    private void openSettingsFrame()
    {
        // Nastavenia sa otvoria len raz; ak už existujú, okno sa presunie dopredu.
        if(settingsFrame == null)
        {
            settingsFrame = new SettingsFrame();

            settingsFrame.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosed(WindowEvent e)
                {
                    settingsFrame = null;
                }
            });

            settingsFrame.setVisible(true);
        }
        else
        {
            settingsFrame.setState(Frame.NORMAL);
            settingsFrame.toFront();
            settingsFrame.requestFocus();
        }
    }

    public void openListTeacherFrame(List<Teacher> teachers)
    {
        // Zoznam učiteľov sa otvára iba v jednej inštancii.
        if(listTeachersFrame == null)
        {
            listTeachersFrame = new ListTeachersFrame(teachers);

            listTeachersFrame.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosed(WindowEvent e)
                {
                    listTeachersFrame = null;
                }
            });

            listTeachersFrame.setVisible(true);
        }
        else
        {
            listTeachersFrame.setState(Frame.NORMAL);
            listTeachersFrame.toFront();
            listTeachersFrame.requestFocus();
        }
    }

    private void openAddTeacherFrame()
    {
        // Formulár pridania učiteľa sa otvára iba v jednej inštancii.
        if(addTeacherFrame == null)
        {
            addTeacherFrame = new AddTeacherFrame();

            addTeacherFrame.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosed(WindowEvent e)
                {
                    addTeacherFrame = null;
                }
            });

            addTeacherFrame.setVisible(true);
        }
        else
        {
            addTeacherFrame.setState(Frame.NORMAL);
            addTeacherFrame.toFront();
            addTeacherFrame.requestFocus();
        }
    }

    private void savePdfAs()
    {
        // Predvolená hodnota znamená, že prepísanie zatiaľ nebolo zamietnuté.
        int overwrite = 0;

        try
        {
            // Bez existujúceho PDF nie je čo ukladať.
            if(!Files.exists(AppPath.pdfPath()))
            {
                Svet.sprava("PDF súbor neexistuje. Najprv ho treba vygenerovať.", "Chyba");
                return;
            }

            // Otvorenie systémového dialógu na uloženie PDF.
            String targetFile = Subor.dialogUlozit("Uložiť PDF", "dozor-" + TeacherPdfGenerator.monthName + ".pdf", "*pdf");

            if(targetFile == null) return;

            Path targetPath = Path.of(targetFile);

            // Automatické doplnenie prípony .pdf.
            if(!targetFile.toLowerCase().endsWith(".pdf"))
            {
                targetPath = Path.of(targetFile + ".pdf");
            }

            // Ak súbor existuje, používateľ musí potvrdiť prepísanie.
            if(Files.exists(targetPath))
            {
                overwrite = Svet.otazka("Súbor už existuje. Prajete si ho prepísať?","Prepísať súbor");
            }

            if(overwrite != 0) return;

            // Skopírovanie aktuálneho PDF na zvolené miesto.
            Files.copy(AppPath.pdfPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            Svet.sprava("PDF bolo uložené do:\n" + targetPath, "Hotovo");
        }
        catch (Exception e)
        {
            Svet.sprava("Nepodarilo sa uložiť PDF:\n" + e.getMessage(), "Chyba");
        }
    }

    private void openMonthSettingsFrame()
    {
        // Kalendár sa otvára iba v jednej inštancii.
        if(monthSettingsFrame == null)
        {
            monthSettingsFrame = new MonthSettingsFrame();

            monthSettingsFrame.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosed(WindowEvent e)
                {
                    monthSettingsFrame = null;
                }
            });

            monthSettingsFrame.setVisible(true);
        }
        else
        {
            monthSettingsFrame.setState(Frame.NORMAL);
            monthSettingsFrame.toFront();
            monthSettingsFrame.requestFocus();
        }
    }

    private void saveAppFolderToCloud()
    {
        // Potvrdenie pred odoslaním lokálnych súborov na cloud.
        int confirm = Svet.otazka("Naozaj chcete uložiť súbory aplikácie na cloud?\n\n" + "Cloud záloha pre aktuálny kód školy bude prepísaná.", "Uložiť na cloud");

        if(confirm != 0) return;

        // Cloudová operácia beží mimo hlavného vlákna, aby nezamrzlo UI.
        Thread worker = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    CloudBackupService cloudBackupService = new CloudBackupService();

                    // Nahratie súborov aplikácie na cloud.
                    CloudBackupResult result = cloudBackupService.uploadAppFolder();

                    SwingUtilities.invokeLater(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Svet.sprava("Záloha bola uložená na cloud.\n\n" + "Počet nahratých súborov: " + result.getFileCount(),"Hotovo");
                        }
                    });
                }
                catch(Exception e)
                {
                    SwingUtilities.invokeLater(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Svet.sprava("Nepodarilo sa uložiť zálohu na cloud:\n" + e.getMessage(),"Chyba");
                        }
                    });
                }
            }
        });

        worker.setDaemon(true);
        worker.start();
    }

    private void loadAppFolderFromCloud()
    {
        // Potvrdenie pred prepísaním lokálnych súborov cloudovou verziou.
        int confirm = Svet.otazka(
                "Naozaj chcete načítať súbory z cloudu?\n\n" + "Lokálne súbory aplikácie pre aktuálny kód školy budú prepísané.", "Načítať z cloudu");

        if(confirm != 0) return;

        // Cloudová operácia beží mimo hlavného vlákna, aby nezamrzlo UI.
        Thread worker = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    CloudBackupService cloudBackupService = new CloudBackupService();

                    // Stiahnutie súborov aplikácie z cloudu.
                    CloudBackupResult result = cloudBackupService.downloadAppFolder();

                    SwingUtilities.invokeLater(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            // Po načítaní dát je starý rozpracovaný rozpis neplatný.
                            Main.invalidatePendingDuties();

                            // Obnovenie PDF náhľadu podľa nových lokálnych súborov.
                            Main.refreshCurrentPdfPreview();

                            Svet.sprava("Súbory boli načítané z cloudu.\n\n" + "Počet stiahnutých súborov: " + result.getFileCount(),"Hotovo");
                        }
                    });
                }
                catch(Exception e)
                {
                    SwingUtilities.invokeLater(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Svet.sprava("Nepodarilo sa načítať súbory z cloudu:\n" + e.getMessage(),"Chyba");
                        }
                    });
                }
            }
        });

        worker.setDaemon(true);
        worker.start();
    }
}