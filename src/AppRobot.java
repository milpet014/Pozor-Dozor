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
import java.nio.file.StandardOpenOption;
import java.time.YearMonth;
import java.util.List;

public class AppRobot extends GRobot
{
    private AddTeacherFrame addTeacherFrame;
    private ListTeachersFrame listTeachersFrame;
    private MonthSettingsFrame monthSettingsFrame;
    private SettingsFrame settingsFrame;

    public AppRobot()
    {
        skry();
    };

    @Override
    public void volbaTlacidla()
    {
        if(UdajeUdalosti.tlacidlo() == Main.getCloseButton())
        {
            Svet.koniec();
        }

        if(UdajeUdalosti.tlacidlo() == Main.getGenerateButton())
        {
            Main.generateCurrentMonthPreview(true);
        }

        if(UdajeUdalosti.tlacidlo() == Main.getConfirmButton())
        {
            Main.confirmCurrentMonthDuties();
        }

        if(UdajeUdalosti.tlacidlo() == Main.getSaveButton())
        {
            savePdfAs();
        }
    }

    @Override
    public void volbaPolozkyPonuky()
    {
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

        if(UdajeUdalosti.polozkaPonuky() == Main.getHelp())
        {
            Svet.sprava("Verzia: Beta-1.0", "O programe");
        }

        if(UdajeUdalosti.polozkaPonuky() == Main.getDocumentation())
        {
            Svet.sprava("Still not implemented :(", "Dokumentácia");
        }
    }

    private void openSettingsFrame()
    {
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
        int overwrite = 0;

        try
        {
            if(!Files.exists(AppPath.pdfPath()))
            {
                Svet.sprava("PDF súbor neexistuje. Najprv ho treba vygenerovať.", "Chyba");
                return;
            }

            String targetFile = Subor.dialogUlozit("Uložiť PDF", "dozor-" + TeacherPdfGenerator.monthName + ".pdf", "*pdf");

            if(targetFile == null) return;

            Path targetPath = Path.of(targetFile);

            if(!targetFile.toLowerCase().endsWith(".pdf"))
            {
                targetPath = Path.of(targetFile + ".pdf");
            }

            if(Files.exists(targetPath))
            {
                overwrite = Svet.otazka("Súbor už existuje. Prajete si ho prepísať?","Prepísať súbor");
            }

            if(overwrite != 0) return;

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
        int confirm = Svet.otazka(
                "Naozaj chcete uložiť súbory aplikácie na cloud?\n\n" +
                        "Cloud záloha pre aktuálny kód školy bude prepísaná.",
                "Uložiť na cloud"
        );

        if(confirm != 0)
        {
            return;
        }

        Thread worker = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    CloudBackupService cloudBackupService = new CloudBackupService();

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
        int confirm = Svet.otazka(
                "Naozaj chcete načítať súbory z cloudu?\n\n" +
                        "Lokálne súbory aplikácie pre aktuálny kód školy budú prepísané.",
                "Načítať z cloudu"
        );

        if(confirm != 0)
        {
            return;
        }

        Thread worker = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    CloudBackupService cloudBackupService = new CloudBackupService();

                    CloudBackupResult result = cloudBackupService.downloadAppFolder();

                    SwingUtilities.invokeLater(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Main.invalidatePendingDuties();
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


