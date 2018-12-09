package com.wm.ts;

import java.io.IOException;
import java.util.*;

public class BookingTicketServiceMain {

	public static void main(String[] args) {

		TicketServiceImpl theatreTicketService = new TicketServiceImpl();

		System.out.println("*****  Welcome to the ticket Service system.  *****");

		loop: while (true) {

			System.out.println("");
			System.out.println("Enter the appropriate number from below");
			System.out.println("Display the number of seats available by level Wise ====> 1");
			System.out.println("Hold seats by Level Wise ====> 2");
			System.out.println("Reserve seats === 3");
			System.out.println("Exit from the system ====> 4");
			System.out.println();

			Scanner scanner = new Scanner(System.in);
			Integer input;
			try {
				input = scanner.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("Please enter an integer value within the range.");
				break;
			}
			int firstLevelNumber = VenueLevel.values()[0].getLevel();
			int lastIndexValues = VenueLevel.values().length - 1;
			int lastLevelNumber = VenueLevel.values()[lastIndexValues].getLevel();

			switch (input) {

			case (1): {
				theatreTicketService.displaySeatsInAllLevels();
				System.out.println("Please enter the level you would like to check. Enter a variable between "
						+ firstLevelNumber + " and " + lastLevelNumber + ".");
				//String venueLevelString = scanner.nextLine();
				Integer venueLevelInt;
				try {
					venueLevelInt = Integer.parseInt(scanner.nextLine());
				} catch (java.lang.NumberFormatException e) {
					venueLevelInt = null;
				}
				if ((venueLevelInt == null)
						|| (firstLevelNumber <= venueLevelInt && venueLevelInt <= lastLevelNumber)) {
					Optional<Integer> venueLevel = Optional.ofNullable(venueLevelInt);
					int numSeatsAvailable = theatreTicketService.numSeatsAvailable(venueLevel);
					if (venueLevel.isPresent()) {
						System.out.println("There are " + numSeatsAvailable + " available in the level.");
					} else {
						System.out.println("There are " + numSeatsAvailable + " available in the level.");
					}
				} else {
					System.out.println("Number not in range");
				}

				break;
			}

			case 2: {
                try {
					System.out.println("Enter one of the below Level number For Checking Available Seats:");
					System.out.println("ORCHESTRA===>1");
					System.out.println("Main ===>2");
					System.out.println("Balcony1===>3");
					System.out.println("Balcony2===>4");
					int levelnumber = scanner.nextInt();
					switch (levelnumber) {
					case 1: {
						OrchestraLevelSeatHold.seatHold();
						break;
					}
					case 2: {
						MainLevelSeatHold.seatHold();
						break;
					}
					case 3: {
						Balcony1LevelSeatHold.seatHold();
						break;
					}
					case 4: {
						Balcony2LevelSeatHold.seatHold();
						break;
					}
					default: {
						System.out.println("Wrong input. Please try again between the given Levels.");
					}
					
					}
					//System.out.println("Number of seats available after holding seats");
					//theatreTicketService.displaySeatsInAllLevels();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
               /* Integer numSeats;
                try {
                    numSeats = scanner1.nextInt();
                } catch (java.util.InputMismatchException e) {
                    System.out.println("Please enter an integer value.");
                    break;
                }
                System.out.println();
                System.out.println("Please enter the minimum level you would like to reserve seats in. Enter a variable between " + firstLevelNumber + " and " + lastLevelNumber + ".");
                String minVenueLevelString = scanner1.nextLine();
                Integer minVenueLevelInt;
                try {
                    minVenueLevelInt = Integer.parseInt(minVenueLevelString);
                } catch (java.lang.NumberFormatException e) {
                    minVenueLevelInt = null;
                }
                Optional<Integer> minVenueLevel;
                if ((minVenueLevelInt == null) || (firstLevelNumber <= minVenueLevelInt && minVenueLevelInt <= lastLevelNumber)) {
                    minVenueLevel = Optional.ofNullable(minVenueLevelInt);
                    if (minVenueLevel == null) {
                        minVenueLevel = Optional.of(firstLevelNumber);
                    }
                } else {
                    System.out.println("Number not in range");
                    break;
                }

                System.out.println();
               // System.out.println("Please enter the minimum level you would like to reserve seats in. Enter a variable between " + firstLevelNumber + " and " + lastLevelNumber + ".");
                String maxVenueLevelString = scanner1.nextLine();
                Integer maxVenueLevelInt;
                try {
                    maxVenueLevelInt = Integer.parseInt(maxVenueLevelString);
                } catch (java.lang.NumberFormatException e) {
                    maxVenueLevelInt = null;
                }
                Optional<Integer> maxVenueLevel;
                if ((maxVenueLevelInt == null) || (firstLevelNumber <= maxVenueLevelInt && maxVenueLevelInt <= lastLevelNumber)) {
                    maxVenueLevel = Optional.ofNullable(maxVenueLevelInt);
                    if (maxVenueLevel == null) {
                        maxVenueLevel = Optional.of(lastLevelNumber);
                    }
                } else {
                    System.out.println("Number not in range");
                    break;
                }

                System.out.println();
                System.out.println("Please enter the email id of the customer");
                String customerEmail = scanner1.nextLine();
                SeatHold seatHold = theatreTicketService.findAndHoldSeats(numSeats, minVenueLevel, maxVenueLevel, customerEmail);
                System.out.println("Your Seats are kept in Hold Successfully !!!");
				System.out.println("Your seat hold id is:" + theatreTicketService.generateHoldId());
				System.out.println("Number of Seats left after Your Hold Seats:");
				
                theatreTicketService.displaySeatsInAllLevels();
                break;
                */

                
            }


		
			case (3): {
				try {
					System.out.println("Enter one of the below Level number For Checking Available Seats:");
					System.out.println("ORCHESTRA===>1");
					System.out.println("Main ===>2");
					System.out.println("Balcony1===>3");
					System.out.println("Balcony2===>4");
					int levelnumber = scanner.nextInt();
					switch (levelnumber) {
					case 1: {
						OrchestraLevelSeatArrangement.seatReserve();
						break;
					}
					case 2: {
						MainLevelSeatArrangement.seatReserve();
						break;
					}
					case 3: {
						Balcony1LevelSeatArrangement.seatReserve();
						break;
					}
					case 4: {
						Balcony2LevelSeatArrangement.seatReserve();
						break;
					}
					default: {
						System.out.println("Wrong input. Please try again between the given Levels.");
					}
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}

			case (4): {
				System.exit(0);
			}

			default: {
				System.out.println("Wrong input. Please try again between the given values.");
			}

			}
			//scanner.close();

		}

	}

}
