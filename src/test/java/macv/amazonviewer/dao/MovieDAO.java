package macv.amazonviewer.dao;

import macv.amazonviewer.db.IDBConnection;
import macv.amazonviewer.model.Movie;
import static macv.amazonviewer.db.DataBaseParams.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface MovieDAO extends IDBConnection {
    //Método para marcar una película como vista
    default Movie setMovieViewed(Movie movie){
        return movie;
    }

    //Obtener películas de la BD
    default ArrayList<Movie> getMovies(){
        ArrayList<Movie> movies = new ArrayList<>();

        try (Connection connection = getDBConnection()) {

            String query = "SELECT * FROM " + TMOVIE;
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet queryResult = ps.executeQuery();

            while (queryResult.next()){

                String title = queryResult.getString(TMOVIE_TITLE);
                String genre = queryResult.getString(TMOVIE_GENRE);
                String creator = queryResult.getString(TMOVIE_CREATOR);
                int duration = queryResult.getInt(TMOVIE_DURATION);
                short year = (short) queryResult.getInt(TMOVIE_YEAR);
                Movie movie = new Movie(title,genre,creator, duration, year);
                movie.setId(queryResult.getInt(TMOVIE_ID));

                movie.setViewed(getMovieViewed(movie, connection, TUSER_IDUSUARIO, ID_TMATERIALS[0]));
                movies.add(movie);
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

        return movies;
    }

    //En la bd se creará una tabla con las películas vistas por
    //Eso se creará este método
    private boolean getMovieViewed(Movie movie, Connection connection, int userId, int idMaterial){
        String query = "SELECT * FROM " + TVIEWED +
                " WHERE " + TVIEWED_ID_MATERIAL + " =? " +
                " AND " + TVIEWED_ID_ELEMENT + " =? " +
                " AND " + TVIEWED_ID_USER + " =? ";

        System.out.println(query);
        boolean viewed = false;
        try{
            System.out.println(movie.getId());
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, idMaterial);
            ps.setInt(2, movie.getId());
            ps.setInt(3, userId);
            ResultSet result = ps.executeQuery();

            viewed = result.next();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return viewed;
    }
}
