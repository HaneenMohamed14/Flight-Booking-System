public class Flight {
    private String flightNumber;
    private String airline;
    private String origin;
    private String destination;
    private String departureTime;
    private String arrivalTime;
    private double economyPrice;
    private double businessPrice;
    private double firstClassPrice;
    private int totalEconomySeats;
    private int totalBusinessSeats;
    private int totalFirstClassSeats;

    // Constructor
    public Flight( String flightNumber, String airline, String origin, String destination, String departureTime, String arrivalTime, 
                  double economyPrice, double businessPrice, double firstClassPrice, int totalEconomySeats, int totalBusinessSeats, int totalFirstClassSeats) {
        
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.economyPrice = economyPrice;
        this.businessPrice = businessPrice;
        this.firstClassPrice = firstClassPrice;
        this.totalEconomySeats = totalEconomySeats;
        this.totalBusinessSeats = totalBusinessSeats;
        this.totalFirstClassSeats = totalFirstClassSeats;
    }
    public Flight(){}

    // Check availability of seats in all classes
    public boolean checkAvailability(String seatClass) {
        switch (seatClass.toLowerCase()) {
            case "economy":
                return totalEconomySeats > 0;
            case "business":
                return totalBusinessSeats > 0;
            case "first":
                return totalFirstClassSeats > 0;
            default:
                return false;
        }
    }

    // Update flight schedule (departure time and arrival time)
    public void updateSchedule(String newDepartureTime, String newArrivalTime) {
        this.departureTime = newDepartureTime;
        this.arrivalTime = newArrivalTime;
    }

    // Calculate the price based on seat class
    public double calculatePrice(String seatClass) {
        switch (seatClass.toLowerCase()) {
            case "economy":
                return economyPrice;
            case "business":
                return businessPrice;
            case "first":
                return firstClassPrice;
            default:
                return 0.0;
        }
    }

    // Reserve a seat for a specific class and reduce the available seats
    public boolean reserveSeat(String seatClass) {
        if (checkAvailability(seatClass)) {
            switch (seatClass.toLowerCase()) {
                case "economy":
                    totalEconomySeats--;
                    break;
                case "business":
                    totalBusinessSeats--;
                    break;
                case "first":
                    totalFirstClassSeats--;
                    break;
                default:
                    return false;
            }
            return true;
        }
        return false;
    }

    // Getters and Setters for all fields...

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }
    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getEconomyPrice() {
        return economyPrice;
    }

    public void setEconomyPrice(double economyPrice) {
        this.economyPrice = economyPrice;
    }

    public double getBusinessPrice() {
        return businessPrice;
    }

    public void setBusinessPrice(double businessPrice) {
        this.businessPrice = businessPrice;
    }

    public double getFirstClassPrice() {
        return firstClassPrice;
    }

    public void setFirstClassPrice(double firstClassPrice) {
        this.firstClassPrice = firstClassPrice;
    }

    public int getTotalEconomySeats() {
        return totalEconomySeats;
    }

    public void setTotalEconomySeats(int totalEconomySeats) {
        this.totalEconomySeats = totalEconomySeats;
    }

    public int getTotalBusinessSeats() {
        return totalBusinessSeats;
    }

    public void setTotalBusinessSeats(int totalBusinessSeats) {
        this.totalBusinessSeats = totalBusinessSeats;
    }

    public int getTotalFirstClassSeats() {
        return totalFirstClassSeats;
    }

    public void setTotalFirstClassSeats(int totalFirstClassSeats) {
        this.totalFirstClassSeats = totalFirstClassSeats;
    }
    public String toString() {
        return 
               ", airline=" + airline + ", origin=" + origin + ", destination=" + destination + 
               ", departureTime=" + departureTime + ", arrivalTime=" + arrivalTime + 
               ", economyPrice=" + economyPrice + ", businessPrice=" + businessPrice + 
               ", firstClassPrice=" + firstClassPrice + ", totalEconomySeats=" + totalEconomySeats + 
               ", totalBusinessSeats=" + totalBusinessSeats + ", totalFirstClassSeats=" + totalFirstClassSeats + "]";
    }
}