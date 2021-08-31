package com.company;
import com.github.javafaker.Faker;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.TextInputDialogBuilder;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import java.io.IOException;
import static com.googlecode.lanterna.gui2.dialogs.MessageDialogButton.*;
import static jdk.nashorn.internal.objects.NativeMath.round;

class Terminala {
    public static void TerminalKCK() throws IOException {
        Terminal terminal = new DefaultTerminalFactory().createTerminal();

        Screen screen = new TerminalScreen(terminal);
        screen.doResizeIfNecessary();
        screen.startScreen();

        final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
        // Create panel to hold components
        Panel panel = new Panel();
        //panel.setPreferredSize(new TerminalSize(100,10));
        panel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));

        Panel lewy = new Panel();
        panel.addComponent(lewy.withBorder(Borders.singleLine("Dostępne opcje")));
        lewy.setLayoutManager(new GridLayout(2));

        Panel prawy = new Panel();
        prawy.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        panel.addComponent(prawy.withBorder(Borders.singleLine("Lista studentów i książek")));

        //labele w którcyh nic nie ma aby troche uporządkować wygląd
        final Label spacja = new Label(" ");
        final Label spacja2 = new Label(" ");
        final Label informacja = new Label("Naciśnij enter na wybranej pozycji\naby zobaczeć dokładne informacje");
        final Label spacja3 = new Label(" ");

        //inicjalizacja tabel do wyświetlania informacji
        Table<String> table_student = new Table<String>("Imię", "Nazwisko", "Nr indeksu");
        Table<String> table2_ksiazka = new Table<String>("Tytuł","Autor","Cena");
        lewy.addComponent(informacja);
        lewy.addComponent(spacja3);
        prawy.addComponent(table_student);
        prawy.addComponent(spacja);
        prawy.addComponent(table2_ksiazka);

        table_student.setVisibleRows(7);
        table2_ksiazka.setVisibleRows(7);

        Random random = new Random();

        ArrayList<Student> dane_studentow = new ArrayList<Student>();
        List<Book> ksiazki = new ArrayList<Book>();
        for(int i=0;i<100;i++){
            Faker faker = new Faker();
            String name = faker.name().firstName();//losowe imię
            String surname = faker.name().lastName();//losowe nazwisko
            String autor = faker.name().fullName();//losowanie autora
            String title = faker.book().title();//losowy tytuł książki
            int indesk = random.nextInt(10000);
            int ilosc_wyporz_ksiazek = random.nextInt(10);
            double cena = random.nextDouble() * 25;
            int rok_studiow = random.nextInt(6);
            int rok_wydania = random.nextInt(2020 - 1800) + 1800;//(max-min)+min
            int ilosc_na_stanie = random.nextInt(100);
            dane_studentow.add(new Student(name,surname,indesk, rok_studiow, ilosc_wyporz_ksiazek));
            ksiazki.add(new Book(title,autor,rok_wydania,round(2,cena),ilosc_na_stanie));
        }

        //dodawanie wstępnych studentów
        for (Student student : dane_studentow) {
            String nr = Integer.toString(student.getNr_ideksu());
            table_student.getTableModel().addRow(student.getName(), student.getNazwisko(), nr);
        }

        //dodawanie wstępnych książek
        for (Book ksiazka : ksiazki){
            String prize = Double.toString(ksiazka.getPrize());
            table2_ksiazka.getTableModel().addRow(ksiazka.getTitle(), ksiazka.getAutor(), prize);
        }

        //pełne informacje na temant studenta
        table_student.setSelectAction(new Runnable() {
            @Override
            public void run() {
                int i = table_student.getSelectedRow();
                String imie = dane_studentow.get(i).getName();
                String nazwisko = dane_studentow.get(i).getNazwisko();
                String indeks = Integer.toString(dane_studentow.get(i).getNr_ideksu());
                String rok_studiow = Integer.toString(dane_studentow.get(i).getRok_studiow());
                String ilosc_wuporzyczen = Integer.toString(dane_studentow.get(i).getIlosc_wyporz_ksiazek());
                new MessageDialogBuilder()
                        .setTitle("Dane studenta")
                        .setText("Imię: " + imie + "" +
                                "\nNazwisko: " + nazwisko + "" +
                                "\nNumer Indeksu:  " + indeks + "" +
                                "\nRok studiów: " + rok_studiow + "" +
                                "\nIlość wyporzyczonych książek:  " + ilosc_wuporzyczen)
                        .addButton(OK)
                        .build()
                        .showDialog(textGUI);
            }
        });

        //pełne informacje na temat książki
        table2_ksiazka.setSelectAction(new Runnable() {
            @Override
            public void run() {
                int j = table2_ksiazka.getSelectedRow();
                String tytul = ksiazki.get(j).getTitle();
                String autor = ksiazki.get(j).getAutor();
                String rok = Integer.toString(ksiazki.get(j).getYear());
                String cena = Double.toString(ksiazki.get(j).getPrize());
                String count = Integer.toString(ksiazki.get(j).getCount());
                new MessageDialogBuilder()
                        .setTitle("Dane książki")
                        .setText("Tytul książki: " + tytul + "" +
                                "\nAutor: " + autor + "" +
                                "\nRok wydania:  " + rok + "" +
                                "\nCena: " + cena + "" +
                                "\nDostępna ilość:  " + count)
                        .addButton(OK)
                        .build()
                        .showDialog(textGUI);
            }
        });

        //dodawanie studenta
        new Button("Dodaj studenta", new Runnable() {
            @Override
            public void run() {

                String imie = new TextInputDialogBuilder()
                        .setTitle("Dodaj")
                        .setDescription("Imie")
                        .setValidationPattern(Pattern.compile("[a-zA-Z]*"),"wpisz imie")
                        .build()
                        .showDialog(textGUI);
                String nazwisko = new TextInputDialogBuilder()
                        .setTitle("Dodaj")
                        .setDescription("Nazwisko")
                        .setValidationPattern(Pattern.compile("[a-zA-Z]*"),"wpisz nazwisko")
                        .build()
                        .showDialog(textGUI);
                String indeks = new TextInputDialogBuilder()
                        .setTitle("Dodaj")
                        .setDescription("Numer Indeksu")
                        .setValidationPattern(Pattern.compile("[0-9]*"),"wpisz indeks")
                        .build()
                        .showDialog(textGUI);
                String rok = new TextInputDialogBuilder()
                        .setTitle("Dodaj")
                        .setDescription("Rok Studiow")
                        .setValidationPattern(Pattern.compile("[0-9]*"),"wpisz rok studiow")
                        .build()
                        .showDialog(textGUI);
                String ilosc = new TextInputDialogBuilder()
                        .setTitle("Dodaj")
                        .setDescription("Ilość wyporzyczonych książek")
                        .setValidationPattern(Pattern.compile("[0-9]*"),"wpisz ilość wyporzyczonych książek")
                        .build()
                        .showDialog(textGUI);
                int indeksint = Integer.parseInt(indeks);
                int rokSt = Integer.parseInt(rok);
                int iloscwyporz = Integer.parseInt(ilosc);
                dane_studentow.add(new Student(imie,nazwisko,indeksint,rokSt,iloscwyporz));
                table_student.getTableModel().addRow(imie,nazwisko,indeks);
            }
        }).addTo(lewy);

        //usuwanie studenta
        new Button("Usun studenta", new Runnable() {
            @Override
            public void run() {
                String nrindeks = new TextInputDialogBuilder()
                        .setTitle("Usun")
                        .setDescription("Poprzez numer indeksu")
                        .setValidationPattern(Pattern.compile("[0-9]*"),"wpisz numer indeksu")
                        .build()
                        .showDialog(textGUI);
                int indeksnr = Integer.parseInt(nrindeks);
                boolean usun = false;
                int studnr = 0;
                for (Student student : dane_studentow) {
                    int numerindeksuint = student.getNr_ideksu();
                    studnr++;
                    if (numerindeksuint == indeksnr) {
                        usun = true;
                        break;
                    }
                }
                if(usun){
                    table_student.getTableModel().removeRow(studnr - 1);
                    dane_studentow.remove(studnr - 1);
                    table_student.setVisibleRows(7);
                    table2_ksiazka.getRenderer();
                }
            }
        }).addTo(lewy);

        //dodawanie książki
        new Button("Dodaj książkę", new Runnable() {
            @Override
            public void run() {
                String tytul = new TextInputDialogBuilder()
                        .setTitle("Dodaj")
                        .setDescription("Tytuł")
                        .setValidationPattern(Pattern.compile("[a-zA-Z]*"),"wpisz tytuł")
                        .build()
                        .showDialog(textGUI);
                String autor = new TextInputDialogBuilder()
                        .setTitle("Dodaj")
                        .setDescription("Autora")
                        .setValidationPattern(Pattern.compile("[a-zA-Z]*"),"wpisz autora")
                        .build()
                        .showDialog(textGUI);
                String rok = new TextInputDialogBuilder()
                        .setTitle("Dodaj")
                        .setDescription("Rok wydania")
                        .setValidationPattern(Pattern.compile("[0-9]*"),"wpisz rok wydania")
                        .build()
                        .showDialog(textGUI);
                String cena = new TextInputDialogBuilder()
                        .setTitle("Dodaj")
                        .setDescription("Cenę")
                        .setValidationPattern(Pattern.compile("[0-9]*"),"wpisz cenę")
                        .build()
                        .showDialog(textGUI);
                String dostepna_ilosc = new TextInputDialogBuilder()
                        .setTitle("Dodaj")
                        .setDescription("Ilość na magazynie")
                        .setValidationPattern(Pattern.compile("[0-9]*"),"wpisz ilość na stanie")
                        .build()
                        .showDialog(textGUI);
                int year = Integer.parseInt(rok);
                double prize = Double.parseDouble(cena);
                int amount = Integer.parseInt(dostepna_ilosc);
                ksiazki.add(new Book(tytul,autor,year,prize,amount));
                table2_ksiazka.getTableModel().addRow(tytul,autor,cena);
            }
        }).addTo(lewy);

        //usuwanie książki (to jest wszystkie egzemplarze) za pomocą tytułu i autora
        new Button("Usun ksiązkę", new Runnable() {
            @Override
            public void run() {
                String tytul = new TextInputDialogBuilder()
                        .setTitle("Usuń wszystkie egzemplarze")
                        .setDescription("Podaj tytuł")
                        .setValidationPattern(Pattern.compile("[a-zA-Z]*"),"podaj tytuł")
                        .build()
                        .showDialog(textGUI);
                String autor = new TextInputDialogBuilder()
                        .setTitle("Usuń wszystkie egzemplarze")
                        .setDescription("Podaj autora książki")
                        .setValidationPattern(Pattern.compile("[a-zA-Z0-9]*"),"podaj autora")
                        .build()
                        .showDialog(textGUI);
                boolean usun = false;
                int ksiazkanumer = 0;
                for(Book ksiazka : ksiazki){
                    String tytul_ksiazki = ksiazka.getTitle();
                    String autor_ksiazki = ksiazka.getAutor();
                    ksiazkanumer++;
                    if(tytul.equals(tytul_ksiazki) && autor.equals(autor_ksiazki)){
                        usun = true;
                        break;
                    }
                }
                if(usun){
                    table2_ksiazka.getTableModel().removeRow(ksiazkanumer - 1);
                    ksiazki.remove(ksiazkanumer - 1);
                    table2_ksiazka.setVisibleRows(7);
                    table2_ksiazka.getRenderer();
                }
            }
        }).addTo(lewy);

        //wyporzyczenie / usuwanie jednego z dostępnych egzemplarzy
        new Button("Wyporzycz książkę", new Runnable() {
            @Override
            public void run() {
                String tytul = new TextInputDialogBuilder()
                        .setTitle("Usuń wszystkie egzemplarze")
                        .setDescription("Podaj tytuł")
                        .setValidationPattern(Pattern.compile("[a-zA-Z]*"),"podaj tytuł")
                        .build()
                        .showDialog(textGUI);
                String autor = new TextInputDialogBuilder()
                        .setTitle("Usuń wszystkie egzemplarze")
                        .setDescription("Podaj autora książki")
                        .setValidationPattern(Pattern.compile("[a-zA-Z0-9]*"),"podaj autora")
                        .build()
                        .showDialog(textGUI);
                int ilosc_na_stanie = 0;
                for(Book ksiazka : ksiazki){
                    String tytul_ksiazki = ksiazka.getTitle();
                    String autor_ksiazki = ksiazka.getAutor();
                    if(tytul.equals(tytul_ksiazki) && autor.equals(autor_ksiazki)){
                        ilosc_na_stanie = ksiazka.getCount();
                        if(ilosc_na_stanie != 0){
                            ksiazka.setCount(ilosc_na_stanie);
                            break;
                        }
                        else{
                            new MessageDialogBuilder()
                                    .setTitle("Niepowodzenie")
                                    .setText("Książka nie jest narazie dostępna")
                                    .addButton(OK)
                                    .build()
                                    .showDialog(textGUI);
                        }
                    }
                }
            }
        }).addTo(lewy);

        //zwracanie książki
        new Button("Zwróć książkę", new Runnable() {
            @Override
            public void run() {
                String tytul = new TextInputDialogBuilder()
                        .setTitle("Usuń wszystkie egzemplarze")
                        .setDescription("Podaj tytuł")
                        .setValidationPattern(Pattern.compile("[a-zA-Z]*"), "podaj tytuł")
                        .build()
                        .showDialog(textGUI);
                String autor = new TextInputDialogBuilder()
                        .setTitle("Usuń wszystkie egzemplarze")
                        .setDescription("Podaj autora książki")
                        .setValidationPattern(Pattern.compile("[a-zA-Z0-9]*"), "podaj autora")
                        .build()
                        .showDialog(textGUI);
                int ilosc_na_stanie = 0;
                for (Book ksiazka : ksiazki) {
                    String tytul_ksiazki = ksiazka.getTitle();
                    String autor_ksiazki = ksiazka.getAutor();
                    if (tytul.equals(tytul_ksiazki) && autor.equals(autor_ksiazki)) {
                        ilosc_na_stanie = ksiazka.getCount();
                        ilosc_na_stanie++;
                        ksiazka.setCount(ilosc_na_stanie);
                        new MessageDialogBuilder()
                                .setTitle("Potwierdzenie")
                                .setText("Książka została zwrócona")
                                .addButton(OK)
                                .build()
                                .showDialog(textGUI);
                        break;
                    }
                }
            }
        }).addTo(lewy);

        //kupno książki
        new Button("Kup książkę", new Runnable() {
            @Override
            public void run() {
                String tytul = new TextInputDialogBuilder()
                        .setTitle("Usuń wszystkie egzemplarze")
                        .setDescription("Podaj tytuł")
                        .setValidationPattern(Pattern.compile("[a-zA-Z]*"), "podaj tytuł")
                        .build()
                        .showDialog(textGUI);
                String autor = new TextInputDialogBuilder()
                        .setTitle("Usuń wszystkie egzemplarze")
                        .setDescription("Podaj autora książki")
                        .setValidationPattern(Pattern.compile("[a-zA-Z0-9]*"), "podaj autora")
                        .build()
                        .showDialog(textGUI);
                int ilosc_na_stanie = 0;
                for(Book ksiazka : ksiazki){
                    String tytul_ksiazki = ksiazka.getTitle();
                    String autor_ksiazki = ksiazka.getAutor();
                    if(tytul.equals(tytul_ksiazki) && autor.equals(autor_ksiazki)){
                        ilosc_na_stanie = ksiazka.getCount();
                        if(ilosc_na_stanie != 0){
                            ilosc_na_stanie = ksiazka.getCount() - 1;
                            ksiazka.setCount(ilosc_na_stanie);
                            new MessageDialogBuilder()
                                    .setTitle("Potwierdzenie")
                                    .setText("Wkrótce dostaniesz dowód do zapłaty")
                                    .addButton(OK)
                                    .build()
                                    .showDialog(textGUI);
                            break;
                        }
                        else{
                            new MessageDialogBuilder()
                                    .setTitle("Potwierdzenie")
                                    .setText("Takiej książki nie ma lub nie ma jej w magazynie")
                                    .addButton(OK)
                                    .build()
                                    .showDialog(textGUI);
                        }
                    }
                }
            }
        }).addTo(lewy);
        lewy.addComponent(spacja2);

        //szukanie STUDENTÓW
        Label indeks_szukaj = new Label("Wyszukaj studenta po \nnumerze indeksu");
        lewy.addComponent(indeks_szukaj);
        TextBox szukaj_indeks = new TextBox().setValidationPattern(Pattern.compile("[0-9]*")).addTo(lewy);

        new Button("Wyszukaj", new Runnable() {
            @Override
            public void run() {
                String x = szukaj_indeks.getText();
                if(x.equals("")){
                    new MessageDialogBuilder()
                            .setTitle("Coś poszło nie tak")
                            .setText("Nic nie zostało wpisane\ndo pola wyszukiwania")
                            .addButton(OK)
                            .build()
                            .showDialog(textGUI);
                }else{
                    table_student.getTableModel().clear();
                    int numer = Integer.parseInt(szukaj_indeks.getText());
                    boolean bool = false;
                    String a = null;
                    String b = null;
                    String numerstring = null;
                    for(Student student : dane_studentow){
                        if( student.getNr_ideksu() == numer) {
                            numerstring = Integer.toString(student.getNr_ideksu());
                            bool = true;
                            a = student.getName();
                            b = student.getNazwisko();
                        }
                    }
                    if(bool){
                        table_student.getTableModel().addRow(a,b,numerstring);
                    }
                }
            }
        }).addTo(lewy);

        new Button("Zakończ szukanie", new Runnable() {
            @Override
            public void run() {
                szukaj_indeks.setText("");
                table_student.getTableModel().clear();

                //przywracanie studentów do tabeli
                for (Student student : dane_studentow) {
                    String nr = Integer.toString(student.getNr_ideksu());
                    table_student.getTableModel().addRow(student.getName(), student.getNazwisko(), nr);
                }
            }
        }).addTo(lewy);

        //wyszukiwanie KSIĄŻEK
        Label szukaj_tytul = new Label("Wyszukaj książkę za \npomocą tytułu");
        lewy.addComponent(szukaj_tytul);
        TextBox tutyl_szukaj = new TextBox().setValidationPattern(Pattern.compile("[a-zA-Z]*")).addTo(lewy);


        new Button("Wyszukaj", new Runnable() {
            @Override
            public void run() {
                String x = szukaj_indeks.getText();
                if(x.equals("")){
                    new MessageDialogBuilder()
                            .setTitle("Coś poszło nie tak")
                            .setText("Nic nie zostało wpisane\ndo pola wyszukiwania")
                            .addButton(OK)
                            .build()
                            .showDialog(textGUI);
                }else{
                    table2_ksiazka.getTableModel().clear();
                    String tytul = tutyl_szukaj.getText();
                    String autor = null;
                    double cena = 0.0;
                    boolean bool = false;
                    for(Book ksiazka : ksiazki){
                        if(tytul.equals(ksiazka.getTitle())){
                            bool = true;
                            autor = ksiazka.getAutor();
                            cena = ksiazka.getPrize();
                        }
                    }
                    if(bool){
                        String prize = Double.toString(cena);
                        table2_ksiazka.getTableModel().addRow(tytul,autor,prize);
                    }
                }
            }
        }).addTo(lewy);

        //usuwanie filtrów książek powrót do normalnej tabeli
        new Button("Zakończ szukanie", new Runnable() {
            @Override
            public void run() {
                tutyl_szukaj.setText("");
                table2_ksiazka.getTableModel().clear();

                //przywracanie studentów do tabeli
                for (Book ksiazka : ksiazki){
                    String prize = Double.toString(ksiazka.getPrize());
                    table2_ksiazka.getTableModel().addRow(ksiazka.getTitle(), ksiazka.getAutor(), prize);
                }
            }
        }).addTo(lewy);

        // Create window to hold the panel

        BasicWindow window = new BasicWindow();
        window.setComponent(panel.withBorder(Borders.singleLine("Panel Główny")));

        //przycisk do zamknięcia terminala
        new Button("Wyjście", new Runnable() {
            @Override
            public void run() {
                try {
                    terminal.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).addTo(lewy);

        // Create gui and start gui
        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(),
                                 new EmptySpace(TextColor.ANSI.BLUE));
        gui.addWindowAndWait(window);
    }
}
