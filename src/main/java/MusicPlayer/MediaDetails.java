/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicPlayer;

/**
 * Plain Old Java Object (POJO) class for storing media details that will be displayed in the view.
 * Class has to be instantiated from public MediaDetailsBuilder class.
 * @author KuRi
 */

public class MediaDetails {
    
    private final String audioResourceFilePath;
    private final String fileName;
    private final String author;
    private final String title;
    private final String album;
    private final String genre;
    private final String dateReleased;
    private final String fileTypeExtension;
    
    private final int trackNumber;
    private final long playTime;
    
    private MediaDetails(MediaDetailsBuilder details){
        
        audioResourceFilePath = details.audioResourceFilePath;
        fileName = details.fileName;
        fileTypeExtension = details.fileTypeExtension;
        
        author = details.author;
        title = details.title;
        album = details.album;
        genre = details.genre;
        dateReleased = details.dateReleased;

        trackNumber = details.trackNumber;
        playTime = details.playTime;
    }
    
    
    //getters
    public String getFilePath(){
        return this.audioResourceFilePath;
    }
    
    public String getFileName(){
        return this.fileName;
    }
    
    public String getFileTypeExtension(){
        return this.fileTypeExtension;
    }
    
    public String getAuthor(){
        return this.author;
    }
    
    public String getTitle(){
        return this.title;
    }

    public String getAlbum(){
        return this.album;
    }
    
    public String getGenre(){
        return this.genre;
    }

    public String getReleaseDate(){
        return this.dateReleased;
    }
    
    public int getTrackNumber(){
        return this.trackNumber;
    }
    
    public long getPlayTime(){
        return this.playTime;
    }
    
    @Override
    public String toString(){
        String serialized = String.format("File Path: %s\nFile Name: %s\nAuthor: %s\nFile Extension: %s\n"
                                         +"Title: %s\nAlbum: %s\nTrack #: %d\nGenre: %s\nPlay time: %d\n", 
                                          this.audioResourceFilePath,
                                          this.fileName,
                                          this.author,
                                          this.fileTypeExtension,
                                          this.title,
                                          this.album,
                                          this.trackNumber,
                                          this.genre,
                                          this.playTime             
                                        );
        return serialized;
    }
    
    
    public static class MediaDetailsBuilder{
        private final String audioResourceFilePath;
        private final String fileName;
        private final String fileTypeExtension;
        
        private String author = "";
        private String title = "";
        private String album = "";
        private String genre = "";
        private String dateReleased = "";
        
        private int trackNumber = 0;
        private long playTime = 0;
        
        public MediaDetailsBuilder(String filePath, String fileName, String fileTypeExtension){
            this.audioResourceFilePath = filePath;
            this.fileName = fileName;
            this.fileTypeExtension = fileTypeExtension;
        }
        
        public MediaDetails build(){
            return new MediaDetails(this);
        }
        
        //field settings
        
        public MediaDetailsBuilder author(String author){
            this.author = author;
            return this;
        }
        
        public MediaDetailsBuilder title(String title){
            this.title = title;
            return this;
        }
        
        public MediaDetailsBuilder album(String album){
            this.album = album;
            return this;
        }
        
        public MediaDetailsBuilder genre(String genre){
            this.genre = genre;
            return this;
        }
        
        public MediaDetailsBuilder dateReleased(String dateReleased){
            this.dateReleased = dateReleased;
            return this;
        }
        
        public MediaDetailsBuilder trackNumber(int trackNumber){
            this.trackNumber = trackNumber;
            return this;
        }
        
        public MediaDetailsBuilder playTime(long playTime){
            this.playTime = playTime;
            return this;
        }  
        
    }
    
}
