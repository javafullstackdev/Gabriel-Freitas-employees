package com.example.application.views.csv;

import com.example.application.views.MainLayout;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

@PageTitle("CSV")
@Route(value = "CSV", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class CSVView extends VerticalLayout {
    Grid<String[]> grid=new Grid<>();
    public String [] employeeId;
    public String [] projectId;
    public String [] dateFrom;
    public String[] dateTo;
    Solution solution= new Solution();
    public CSVView() {
        var importButton=new Button("import from classpath", e->readFromClasspath());
        var buffer = new MemoryBuffer();
        var upload =new Upload(buffer);

        upload.addSucceededListener(e->{
            displayCsv(buffer.getInputStream());

        });
        add(grid, new HorizontalLayout(importButton, upload));
        setSpacing(false);



        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    private void readFromClasspath(){
        displayCsv(getClass().getClassLoader().getResourceAsStream("MyCSV.csv"));
    }

    private void displayCsv(InputStream resourceAsStream) {
        var parser = new CSVParserBuilder().withSeparator(',').build();
        var reader =new CSVReaderBuilder(new InputStreamReader(resourceAsStream)).withCSVParser(parser).build();

        try{
            var records =reader.readAll();
            if(records.size()>1) {
                var entries = solution.getLongestPair(records);
                var headers = entries.get(0);
                for (int i = 0; i < headers.length; i++) {
                    int colIndex = i;
                    grid.addColumn(row -> row[colIndex])
                            .setHeader(headers[colIndex]);
                }
                if (entries.size() > 1)
                    grid.setItems(entries.subList(1, entries.size()));
            }




        } catch (IOException | CsvException e){
            e.printStackTrace();
        }
    }



}
