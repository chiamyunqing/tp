package seedu.address.model.room;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.model.room.exceptions.DuplicateRoomException;
import seedu.address.model.room.exceptions.RoomNotFoundException;
import seedu.address.storage.JsonAddressBookStorage;

/**
 * Contains information regarding the Room information
 */
public class RoomList {
    private static final Logger logger = LogsCenter.getLogger(JsonAddressBookStorage.class);

    private int numOfRooms;
    private PriorityQueue<Room> rooms = new PriorityQueue<>();
    private Room[] roomsInArray = new Room[0];
    private ObservableList<Room> roomObservableList = FXCollections.observableArrayList();

    /** Creates default RoomList() object where all fields are null**/
    public RoomList() {}

    /**
     * Creates a RoomList object using the information given in files containing information about
     * which rooms are occupied and number of rooms
     */
    public RoomList(PriorityQueue<Room> rooms, Room[] roomsInArray, int numOfRooms) {
        this.rooms = rooms;
        this.roomsInArray = roomsInArray;
        this.numOfRooms = numOfRooms;
        convertPriorityQueue(rooms);
    }

    /**
     * Returns Priority Queue of rooms
     */
    public PriorityQueue<Room> getRooms() {
        return this.rooms;
    }

    /**
     * Returns number of rooms in hotel
     */
    public int getNumOfRooms() {
        return numOfRooms;
    }

    public ObservableList<Room> getRoomObservableList() {
        return roomObservableList;
    }

    private void addRooms() {
        if (numOfRooms > 0) {
            roomsInArray = new Room[numOfRooms];
            rooms = new PriorityQueue<>();
            for (int i = 0; i < numOfRooms; i++) {
                Room room = new Room(i + 1);
                rooms.add(room);
                roomsInArray[i] = room;
            }
        }
        convertPriorityQueue(rooms);
    }

    /**
     * Adds the number of the rooms in a hotel
     *
     * @param numOfRooms is the number of rooms to be added
     */
    public void addRooms(int numOfRooms) {
        this.numOfRooms = numOfRooms;
        addRooms();
    }
    /**
     * Sets the elements of {@code roomObservableList}.
     *
     * @param roomList PriorityQueue containing all the rooms.
     */
    private void convertPriorityQueue(PriorityQueue<Room> roomList) {
        ArrayList<Room> roomArrayList = new ArrayList<>();
        Object[] arr = roomList.toArray();
        for (int k = 0; k < arr.length; k++) {
            roomArrayList.add((Room) arr[k]);
        }
        roomObservableList.setAll(roomArrayList);
    }
    public Room[] getRoomsInArray() {
        return this.roomsInArray;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoomList roomList = (RoomList) o;
        if (rooms != null && roomList.rooms != null) {
            PriorityQueue<Room> copy = new PriorityQueue<>(rooms);
            PriorityQueue<Room> copy1 = new PriorityQueue<>(roomList.rooms);
            return numOfRooms == roomList.numOfRooms
                    && equals(copy, copy1)
                    && Arrays.equals(roomsInArray, roomList.roomsInArray);
        } else {
            return numOfRooms == roomList.numOfRooms
                    && Arrays.equals(roomsInArray, roomList.roomsInArray);
        }
    }

    /**
     * Tests whether 2 PriorityQueues are equal by checking whether at each relative position they contain the equal
     * rooms
     */
    public boolean equals(PriorityQueue<Room> rooms1, PriorityQueue<Room> rooms2) {
        if (rooms1.size() != rooms2.size()) {
            return false;
        } else {
            int size = rooms1.size();
            for (int i = 0; i < size; i++) {
                if (!rooms1.poll().equals(rooms2.poll())) {
                    return false;
                }
            }
            return true;
        }
    }
    /**
     * Returns true if the list contains an equivalent room as the given argument.
     */
    public boolean containsRoom(Room toCheck) {
        requireNonNull(toCheck);
        return roomObservableList.stream().anyMatch(toCheck::isSameRoom);
    }

    /**
     * Replaces the room {@code target} in the list with {@code editedRoom}.
     * {@code target} must exist in the list.
     * The room identity of {@code editedRoom} must not be the same as another existing room in the list.
     *
     * @param target Room to be changed.
     * @param editedRoom Room that has been changed.
     */
    public void setSingleRoom(Room target, Room editedRoom) {
        int index = roomObservableList.indexOf(target);
        if (index == -1) {
            throw new RoomNotFoundException();
        }

        if (!target.isSameRoom(editedRoom) && containsRoom(editedRoom)) {
            throw new DuplicateRoomException();
        }
        rooms.remove(target); // this and the next LOC is to replace the room in the priority queue
        rooms.add(editedRoom);
        roomObservableList.set(index, editedRoom);
    }
    @Override
    public int hashCode() {
        int result = Objects.hash(numOfRooms, rooms);
        result = 31 * result + Arrays.hashCode(roomsInArray);
        return result;
    }

    public void setNumOfRooms(int numOfRooms) {
        this.numOfRooms = numOfRooms;
    }

    public void setRooms(PriorityQueue<Room> rooms) {
        this.rooms = rooms;
    }

    public void setRoomsInArray(Room[] roomsInArray) {
        this.roomsInArray = roomsInArray;
    }

    /**
     * Checks if the given room number is present in the application.
     * @param roomNumber to check if it is in the application.
     * @return Index Of room that is found.
     */
    public Index checkIfRoomPresent(Integer roomNumber) {
        Index index = Index.fromZeroBased(0);
        for (int i = 1; i <= roomObservableList.size(); i++) {
            int roomNum = roomObservableList.get(i - 1).getRoomNumber();
            boolean isValidRoom = (Integer.valueOf(roomNum)).equals(roomNumber);
            if (isValidRoom) {
                index = Index.fromZeroBased(i);
                break;
            }
        }
        return index;
    }
}
