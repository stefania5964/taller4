import org.arep.HttpServer;
import org.junit.*;
import java.io.IOException;
import static org.junit.Assert.*;
public class HttpTest {


            @Test
            public void testGetHello() {
                try {
                    String movie = "Guardians"; // Example movie ID
                    String result = HttpServer.getHello(movie);
                    assertNotNull("Result should not be null", result);
                } catch (Exception e) {
                    fail("Exception thrown: " + e.getMessage());
                }
            }
    @Test
    public void shouldSearchMovie() {
        try {
            String search = HttpServer.getHello("Thor");
            String thor = "{\"Title\":\"Thor\",\"Year\":\"2011\",\"Rated\":\"PG-13\",\"Released\":\"06 May 2011\",\"Runtime\":\"115 min\",\"Genre\":\"Action, Fantasy\",\"Director\":\"Kenneth Branagh\",\"Writer\":\"Ashley Miller, Zack Stentz, Don Payne\",\"Actors\":\"Chris Hemsworth, Anthony Hopkins, Natalie Portman\",\"Plot\":\"The powerful but arrogant god Thor is cast out of Asgard to live amongst humans in Midgard (Earth), where he soon becomes one of their finest defenders.\",\"Language\":\"English\",\"Country\":\"United States\",\"Awards\":\"5 wins & 30 nominations\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BOGE4NzU1YTAtNzA3Mi00ZTA2LTg2YmYtMDJmMThiMjlkYjg2XkEyXkFqcGdeQXVyNTgzMDMzMTg@._V1_SX300.jpg\",\"Ratings\":[{\"Source\":\"Internet Movie Database\",\"Value\":\"7.0/10\"},{\"Source\":\"Rotten Tomatoes\",\"Value\":\"77%\"},{\"Source\":\"Metacritic\",\"Value\":\"57/100\"}],\"Metascore\":\"57\",\"imdbRating\":\"7.0\",\"imdbVotes\":\"876,749\",\"imdbID\":\"tt0800369\",\"Type\":\"movie\",\"DVD\":\"01 Jul 2013\",\"BoxOffice\":\"$181,030,624\",\"Production\":\"N/A\",\"Website\":\"N/A\",\"Response\":\"True\"}";
            Assert.assertEquals(thor, search);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void deberiaGuardarEnCache() {
        try {
            // Asegúrate de que cada solicitud se realice por separado y espera las respuestas

            String searchGuardians = HttpServer.getHello("guardians");
            String searchthor = HttpServer.getHello("thor");
            String searchpanteranegra = HttpServer.getHello("pantera negra");
            String searchBarbie = HttpServer.getHello("barbie");

            // Afirmar que el caché contiene las claves y valores esperados

            Assert.assertTrue(HttpServer.cache.containsKey("guardians"));
            Assert.assertTrue(HttpServer.cache.containsKey("thor"));
            Assert.assertTrue(HttpServer.cache.containsKey("pantera negra"));
            Assert.assertTrue(HttpServer.cache.containsKey("barbie"));

            // Afirmar que el tamaño del caché es el esperado
            Assert.assertEquals(4, HttpServer.cache.size()-1);

            String barbie = "{\"Title\":\"Barbie\",\"Year\":\"2023\",\"Rated\":\"PG-13\",\"Released\":\"21 Jul 2023\",\"Runtime\":\"114 min\",\"Genre\":\"Adventure, Comedy, Fantasy\",\"Director\":\"Greta Gerwig\",\"Writer\":\"Greta Gerwig, Noah Baumbach\",\"Actors\":\"Margot Robbie, Ryan Gosling, Issa Rae\",\"Plot\":\"Barbie suffers a crisis that leads her to question her world and her existence.\",\"Language\":\"English, Spanish\",\"Country\":\"United States, United Kingdom\",\"Awards\":\"2 wins & 1 nomination\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BOWIwZGY0OTYtZjUzYy00NzRmLTg5YzgtYWMzNWQ0MmZiY2MwXkEyXkFqcGdeQXVyMTUzMTg2ODkz._V1_SX300.jpg\",\"Ratings\":[{\"Source\":\"Internet Movie Database\",\"Value\":\"7.4/10\"},{\"Source\":\"Rotten Tomatoes\",\"Value\":\"88%\"},{\"Source\":\"Metacritic\",\"Value\":\"80/100\"}],\"Metascore\":\"80\",\"imdbRating\":\"7.4\",\"imdbVotes\":\"238,040\",\"imdbID\":\"tt1517268\",\"Type\":\"movie\",\"DVD\":\"N/A\",\"BoxOffice\":\"$541,907,382\",\"Production\":\"N/A\",\"Website\":\"N/A\",\"Response\":\"True\"}";
            Assert.assertEquals(searchBarbie, barbie);
            String guardians = "{\"Title\":\"Guardians\",\"Year\":\"2009\",\"Rated\":\"Not Rated\",\"Released\":\"24 Jun 2009\",\"Runtime\":\"87 min\",\"Genre\":\"Horror, Sci-Fi\",\"Director\":\"Drew Maxwell\",\"Writer\":\"Drew Maxwell\",\"Actors\":\"Chris Bell, Benjamin Budd, Tylan Canady\",\"Plot\":\"Twilight Cove, a small forgotten town, is besieged by hideous creatures summoned into our dimension. It's only a matter of time before the army of creatures attacks the rest of civilization and wreaks havoc upon the world. At dusk...\",\"Language\":\"English\",\"Country\":\"United States\",\"Awards\":\"N/A\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BMjgyOTkyOTgwMV5BMl5BanBnXkFtZTgwNTM1OTkwMzE@._V1_SX300.jpg\",\"Ratings\":[{\"Source\":\"Internet Movie Database\",\"Value\":\"3.0/10\"}],\"Metascore\":\"N/A\",\"imdbRating\":\"3.0\",\"imdbVotes\":\"292\",\"imdbID\":\"tt0486592\",\"Type\":\"movie\",\"DVD\":\"15 Jul 2008\",\"BoxOffice\":\"N/A\",\"Production\":\"N/A\",\"Website\":\"N/A\",\"Response\":\"True\"}";
            Assert.assertEquals(searchGuardians, guardians);
            String thor = "{\"Title\":\"Thor\",\"Year\":\"2011\",\"Rated\":\"PG-13\",\"Released\":\"06 May 2011\",\"Runtime\":\"115 min\",\"Genre\":\"Action, Fantasy\",\"Director\":\"Kenneth Branagh\",\"Writer\":\"Ashley Miller, Zack Stentz, Don Payne\",\"Actors\":\"Chris Hemsworth, Anthony Hopkins, Natalie Portman\",\"Plot\":\"The powerful but arrogant god Thor is cast out of Asgard to live amongst humans in Midgard (Earth), where he soon becomes one of their finest defenders.\",\"Language\":\"English\",\"Country\":\"United States\",\"Awards\":\"5 wins & 30 nominations\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BOGE4NzU1YTAtNzA3Mi00ZTA2LTg2YmYtMDJmMThiMjlkYjg2XkEyXkFqcGdeQXVyNTgzMDMzMTg@._V1_SX300.jpg\",\"Ratings\":[{\"Source\":\"Internet Movie Database\",\"Value\":\"7.0/10\"},{\"Source\":\"Rotten Tomatoes\",\"Value\":\"77%\"},{\"Source\":\"Metacritic\",\"Value\":\"57/100\"}],\"Metascore\":\"57\",\"imdbRating\":\"7.0\",\"imdbVotes\":\"876,749\",\"imdbID\":\"tt0800369\",\"Type\":\"movie\",\"DVD\":\"01 Jul 2013\",\"BoxOffice\":\"$181,030,624\",\"Production\":\"N/A\",\"Website\":\"N/A\",\"Response\":\"True\"}";
            Assert.assertEquals(searchthor, thor);
            String panteranegra = "{\"Title\":\"Pantera Negra\",\"Year\":\"1968\",\"Rated\":\"N/A\",\"Released\":\"N/A\",\"Runtime\":\"3 min\",\"Genre\":\"Animation, Short\",\"Director\":\"Jô Oliveira\",\"Writer\":\"Jô Oliveira\",\"Actors\":\"N/A\",\"Plot\":\"A hand-painted musical film, it was the first experience with animation cinema by the artist and illustrator Jô Oliveira, at the time a member of the Fotograma group, an organization that brought together the work of several artis...\",\"Language\":\"None\",\"Country\":\"Brazil\",\"Awards\":\"N/A\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BN2ZiOTc4NDItNzM4MS00NzUwLWI2NWYtNTIzNTlkZWEwNGU5XkEyXkFqcGdeQXVyODgxMDAxMjY@._V1_SX300.jpg\",\"Ratings\":[],\"Metascore\":\"N/A\",\"imdbRating\":\"N/A\",\"imdbVotes\":\"6\",\"imdbID\":\"tt12809548\",\"Type\":\"movie\",\"DVD\":\"N/A\",\"BoxOffice\":\"N/A\",\"Production\":\"N/A\",\"Website\":\"N/A\",\"Response\":\"True\"}";
            Assert.assertEquals(searchpanteranegra, panteranegra);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}



