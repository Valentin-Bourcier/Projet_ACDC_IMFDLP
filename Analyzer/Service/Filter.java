package Analyzer.Service;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Analyzer.Control.ErrorManager;

/**
 * Class defining a file filter
 * 
 * @author Valentin Bourcier
 */
public class Filter implements FileFilter
{

    private long weight;
    private boolean weightGt;
    private boolean weightLw;
    private ArrayList<String> extensions;
    private long date;
    private boolean dateGt;
    private boolean dateLw;
    private String name;
    private boolean directory;
    private String pattern;

    /**
     * Filter initialisation
     */
    public Filter()
    {
        extensions = new ArrayList<String>();
        name = null;
        directory = true;
        date = 0;
        weight = 0;
    }

    /**
     * Method checking the correspondance of a file with the filter
     * @return True if the file is valid false either.
     */
    @Override
    public boolean accept(File file)
    {
        boolean accept = true;

        if (file.isDirectory())
        {
            accept = accept && directory;
        }

        if (pattern != null)
        {	
        	try {
        		Pattern regexp = Pattern.compile(pattern);
                Matcher match = regexp.matcher(file.getName());
                accept = accept && match.find();
        	}catch(Exception error){
        		System.out.println("Invalid pattern");
        		error.printStackTrace();
                ErrorManager.throwError(error);
        		System.exit(1);
        	}
        }
        if (weightGt && weight > 0)
        {
            accept = accept && file.length() > weight;
        }
        else if (weightLw && weight > 0)
        {
            accept = accept && file.length() < weight;
        }
        else if (weight > 0)
        {
            accept = accept && file.length() == weight;
        }

        if (dateGt && date > 0)
        {
            accept = accept && file.lastModified() > date;
        }
        else if (dateLw && date > 0)
        {
            accept = accept && file.lastModified() < date;
        }
        else if (date > 0)
        {
            accept = accept && file.lastModified() == date;
        }

        if (!extensions.isEmpty() && !file.isDirectory())
        {
            String extension = "";
            int i = file.getName().lastIndexOf('.');
            if (i > 0)
            {
                extension = file.getName().substring(i + 1);
            }
            accept = extensions.contains(extension);
        }
        if (name != null)
        {
            accept = accept && file.getName().contains(name);
        }
        return accept;
    }

    /**
     * Adding a file name constraint
     * @param comparison String which should be contained in file name
     */
    public void nameContains(String comparison)
    {
        name = comparison;
    }

    /**
     * Adding a file weight equality constraint
     * @param weight Weight that the file should have
     */
    @SuppressWarnings("hiding")
    public void weightEq(long weight)
    {
        this.weight = weight;
        weightGt = false;
        weightLw = false;
    }

    /**
     * Adding a file weight constraint (greater than)
     * @param weight Weight that the file should be greater than
     */
    @SuppressWarnings("hiding")
    public void weightGt(long weight)
    {
        this.weight = weight;
        weightGt = true;
        weightLw = false;
    }

    /**
     * Adding a file weight constraint (lower than)
     * @param weight Weight that the file should be lower than
     */
    @SuppressWarnings("hiding")
    public void weightLw(long weight)
    {
        this.weight = weight;
        weightGt = false;
        weightLw = true;
    }

    /**
     * Adding a valid extension
     * @param extension Extension to accept
     */
    public void acceptExtension(String extension)
    {
        extensions.add(extension);
    }

    /**
     * Adding a date equality constraint
     * @param date Modification date of the file that should be verified
     */
    @SuppressWarnings("hiding")
    public void dateEq(Date date)
    {
        this.date = date.getTime();
        dateGt = false;
        dateLw = false;
    }

    /**
     * Adding a date constraint (Older than)
     * @param date Modification date of the file that should be verified
     */
    @SuppressWarnings("hiding")
    public void dateGt(Date date)
    {
        this.date = date.getTime();
        dateGt = true;
        dateLw = false;
    }

    /**
     * Adding a date constraint (Less older than)
     * @param date Modification date of the file that should be verified
     */
    @SuppressWarnings("hiding")
    public void dateLw(Date date)
    {
        this.date = date.getTime();
        dateGt = false;
        dateLw = true;
    }

    /**
     * Method to check directories
     * @param accept Boolean equals to true if we should check directories validity, false either.
     */
    public void acceptDirectory(boolean accept)
    {
        directory = accept;
    }

    /**
     * Method saying if filter was activate
     * @return True is filter was activated, false either
     */
    public boolean isActive()
    {
        return extensions.isEmpty() == false || name != null || directory == false || date != 0 || weight != 0 || pattern != null;
    }

    /**
     * Method setting a pattern restriction
     * @param pattern A string regexp
     */
    @SuppressWarnings("hiding")
    public void setPattern(String pattern)
    {
        this.pattern = pattern;
    }

}
