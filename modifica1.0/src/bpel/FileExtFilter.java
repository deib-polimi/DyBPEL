package bpel;

import java.io.File;
import java.io.FilenameFilter;

class FileExtFilter implements FilenameFilter
{
    private String estensione;

    public FileExtFilter (String estensione)
    {
        this.estensione = "." + estensione;
    }

    public boolean accept (File dir, String name)
    {
        return name.endsWith (estensione);
    }
}