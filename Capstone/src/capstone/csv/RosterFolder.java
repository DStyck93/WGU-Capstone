package capstone.csv;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import capstone.objects.Season;

/**
 * This class is used to help load the roster files into the system.
 */
public class RosterFolder {
    
    private String directory;
    private Season season;
    private List<File> listOfFiles;

    // Constructors
    public RosterFolder(String directory, Season season, List<File> listOfFiles){
        this.directory = directory;
        this.season = season;
        this.listOfFiles = listOfFiles;
    }

    public RosterFolder(){
        this.directory = null;
        this.season = new Season();
        this.listOfFiles = new ArrayList<>();
    }

    public RosterFolder(String directory){
        this.directory = directory;
        this.season = new Season();
        this.listOfFiles = new ArrayList<>();
    }

    // Getters
    public String getDirectory(){return directory;}
    public Season getSeason(){return season;}
    public List<File> getListOfFiles(){return listOfFiles;}

    // Setters
    public void setDirectory(String directory){this.directory = directory;}
    public void setSeason(Season season){this.season = season;}
    public void setListOfFiles(List<File> listOfFiles){this.listOfFiles = listOfFiles;}

    /**
     * Adds the given file to list of files in the roster folder.
     * @param rosterFolder The folder the file is being added to.
     * @param file The file being added.
     */
    public static void addToListOfFiles(RosterFolder rosterFolder, File file){
        rosterFolder.listOfFiles.add(file);
    }
}
