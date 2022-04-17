package com.movie.store.service;

import com.movie.store.dto.*;
import com.movie.store.exception.CommonException;
import com.movie.store.repository.MovieRepository;
import com.movie.store.repository.RentedMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.time.temporal.ChronoUnit.WEEKS;



/**
 * This class is a service layer
 */
@Service
public class RentedMovieService {

    private final RentedMovieRepository rentedMovieRepository;

    private final MovieRepository movieRepository;


    @Autowired
    public RentedMovieService(RentedMovieRepository rentedMovieRepository,MovieRepository movieRepository) {
        this.rentedMovieRepository = rentedMovieRepository;
        this.movieRepository = movieRepository;
    }


    /**
     * This method returns all movies that were rented.
     * @return list of rented movies in ascending order(ordered by user ID).
     * @throws CommonException if Rented Movie list is empty (rentedmovie table is empty)
     */
    @Transactional
    public List<RentedMovie> getRentedMovies()throws CommonException {
        List<RentedMovie> rentedMovies = new ArrayList<>();
        rentedMovieRepository.findAll(Sort.by(Sort.Direction.ASC, "userId"))
                        .forEach(rentedMovies::add);

        if (rentedMovies.isEmpty()){
            throw new CommonException("Rented Movie list is empty");
        }
        else
            return rentedMovies;
    }


    /**
     * This method returns movies that are rented by a specified user.
     * @param userId is a user ID (required).
     * @return List of movies that a specified user has rented.
     * @throws CommonException if user with given ID is not present in the database
     * @throws CommonException if user with given ID does not have any rented movies.
     */
    @Transactional
    public List<RentedMovie> getRentedMoviesByUserId(Long userId)throws CommonException {
        boolean userExists = rentedMovieRepository.existsByUserId(userId);
        if(!userExists){
            throw new CommonException("User with id: "+userId+ " is not present in the database");
        }


        List<RentedMovie> rentedMovies = rentedMovieRepository.findByUserIdOrderByMovie(userId);

        if (rentedMovies.isEmpty()){
            throw new CommonException("User with id: "+userId+" does not have any rented movies");
        }
        return rentedMovies;
    }


    /**
     * This method returns rented movies by movie ID.
     * @param movieId is a movie ID (required).
     * @return list of rented movies by movie ID.
     * @throws CommonException if movie with given ID does not exist.
     * @throws CommonException if movie with given ID was not rented by anybody.
     */
    @Transactional
    public List<RentedMovie> getRentedMoviesByMovieId(Long movieId)throws CommonException {

        boolean movieExists = movieRepository.existsByMovieId(movieId);

        if(!movieExists){
            throw new CommonException("Movie with ID: "+movieId+" does not exist");
        }

        Movie movie = movieRepository.findMovieByMovieId(movieId);

        List<RentedMovie> rentedMovieListByMovieId = rentedMovieRepository.findByMovie(movie);
        if(rentedMovieListByMovieId.isEmpty()){
            throw new CommonException("Movie with ID: "+movieId+" was not rented by anybody");
        }
        else{
            return rentedMovieListByMovieId;
        }
    }


    /**
     * This method returns a calculated invoice with the price per movie rental and the total amount to pay.
     * @param movieIds List of movie IDs (required).
     * @param timesInWeeks List of weeks that movies are rented (amount of weeks per each movie) (required).
     * @return a calculated invoice with the price per movie rental and the total amount to pay.
     * @throws CommonException if movie with given ID does not exist.
     * @throws CommonException if there are duplicate movie IDs in the request.
     * @throws CommonException if amount of renting weeks per each movie is not present in the request.
     */
    public Invoice calculate(List<Long> movieIds, List<Integer> timesInWeeks)throws CommonException{

        for (Long movieId : movieIds) {
            boolean movieExists = movieRepository.existsByMovieId(movieId);
            if(!movieExists){
                throw new CommonException("Movie with ID: "+movieId+" does not exist");
            }
        }

        Set<Long> set = new HashSet<>(movieIds);

        if(set.size() < movieIds.size()){
            throw new CommonException("There are duplicate movie IDs in the request");
        }

        if(movieIds.size() != timesInWeeks.size()){
            throw new CommonException("Amount of renting weeks per each movie is not present in the request");
        }

        Invoice invoice = new Invoice();
        invoice.setTotalSum(BigDecimal.ZERO);

        List<InvoiceRow> invoiceRows = new ArrayList<>();
        invoice.setInvoiceRows(invoiceRows);
        int count = 0;
        for (Long movieId : movieIds) {
            InvoiceRow invoiceRow = new InvoiceRow();
            invoiceRow.setRentingTimeInWeeks(timesInWeeks.get(count));
            Movie movie = movieRepository.findMovieByMovieId(movieId);
            LocalDate releaseDate = movie.getReleaseDate();

            long weeks = WEEKS.between(releaseDate,LocalDate.now());

            BigDecimal price = BigDecimal.ZERO;

            for (int i=0;i<invoiceRow.getRentingTimeInWeeks();i++){
                if(weeks <= 52){
                    price = price.add(BigDecimal.valueOf(5));
                }
                else if(weeks<156){
                    price = price.add(BigDecimal.valueOf(3.49));
                }
                else{
                    price = price.add(BigDecimal.valueOf(1.99));
                }
                weeks++;
            }

            invoiceRow.setPricePerMovieRental(price);
            invoice.setTotalSum(invoice.getTotalSum().add(invoiceRow.getPricePerMovieRental()));
            invoiceRow.setMovie(movieRepository.findMovieByMovieId(movieId));
            invoiceRows.add(invoiceRow);
            count++;
        }

        return invoice;

    }


    /**
     * This method adds movies that a specified user has rented.
     * @param movieIDs List of movie IDs (required).
     * @param timesInWeeks List of weeks that movies are rented (amount of weeks per each movie) (required).
     * @param userId ID of a user that rents movies (required).
     * @throws CommonException if there are duplicate movie IDs in the request.
     * @throws CommonException if amount of renting weeks per each movie is not present in the request.
     * @throws CommonException if User with specified ID already owns one of the movies requested movies.
     */
    @Transactional
    public void rentMovie(List<Long> movieIDs, List<Integer> timesInWeeks, Long userId)throws CommonException {

        Set<Long> set = new HashSet<>(movieIDs);

        if(set.size() < movieIDs.size()){
            throw new CommonException("There are duplicate movie IDs in the request");
        }

        if(movieIDs.size() != timesInWeeks.size()){
            throw new CommonException("Amount of renting weeks per each movie is not present in the request");
        }

        boolean userIdExists = rentedMovieRepository.existsByUserId(userId);
        if(userIdExists){
            for (Long movieID : movieIDs) {
                Movie movie = movieRepository.findMovieByMovieId(movieID);
                long existances = rentedMovieRepository.countByUserIdAndMovie(userId,movie);
                if(existances > 0){
                    throw new CommonException("User with id: "+userId+" already owns movie with ID: "+movieID);
                }
            }
        }



        Invoice calculatedInvoice = calculate(movieIDs,timesInWeeks);

        List<InvoiceRow> invoiceRows = calculatedInvoice.getInvoiceRows();

        for (InvoiceRow invoiceRow : invoiceRows) {
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = LocalDate.now().plusWeeks(invoiceRow.getRentingTimeInWeeks());

            Movie movie = invoiceRow.getMovie();
            BigDecimal rentalPrice = invoiceRow.getPricePerMovieRental();

            RentedMovie rentedMovie = new RentedMovie(userId,startDate,endDate,rentalPrice,movie);
            rentedMovieRepository.save(rentedMovie);
        }

    }


    /**
     * This method returns movies in descending order (from popular to unpopular).
     * @return returns movies in descending order (from the biggest amount of purchases to the smallest amount).
     */
    public List<Statistics> getPopularMovies() {
        List<Statistics> statisticsList = new ArrayList<>();


        List<List> listList = rentedMovieRepository.findMostPopularMovies();
        for (List list : listList) {
            Statistics statistics = new Statistics();
            statistics.setMovieId((Long)list.get(0));
            statistics.setMovieTitle((String)list.get(1));
            statistics.setPurchases((Long)list.get(2));

            statisticsList.add(statistics);
        }
        
        
        return statisticsList;
        
    }
}
