package seedu.address.model.room;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.stream.IntStream;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.model.ReadOnlyList;
import seedu.address.model.patient.Name;
import seedu.address.model.room.exceptions.DuplicateRoomException;
import seedu.address.model.room.exceptions.RoomNotFoundException;

public class UniqueRoomList implements Iterable<Room> {

    private int numOfRooms;
    private PriorityQueue<Room> rooms = new PriorityQueue<>(new ComparableRoom());
    private final ObservableList<Room> internalList = FXCollections.observableArrayList((Room room) -> {
        Observable[] updatedTasks = new Observable[]{room.getReadOnlyTasks()};
        return updatedTasks;
    });
    private final ObservableList<Room> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    @Override
    public Iterator<Room> iterator() {
        return internalList.iterator();
    }

    /**
     * Resets the existing data of this {@code RoomList} with {@code newData}.
     */
    public void resetData(ReadOnlyList<Room> readOnlyRoomList) {
        requireAllNonNull(readOnlyRoomList);

        ObservableList<Room> roomLists = readOnlyRoomList.getReadOnlyList();
        rooms.addAll(roomLists);
        internalList.addAll(roomLists);
    }

    /**
     * Used it test cases to set rooms with a higher room number which can be more than number of rooms existing
     * in hotel
     * @param room is the room to be input into data
     */
    public void setRoom(Room room) {
        int roomNumber = room.getRoomNumber();
        if (room.getRoomNumber() > internalList.size()) {
            setRoomForRoomNumberLessThanNumberOfRooms(room);
        } else {
            setRoomForRoomNumberMoreThanNumberOfRooms(room);
        }
    }
    private void setRoomForRoomNumberLessThanNumberOfRooms(Room room) {
        int roomNumber = room.getRoomNumber();
        for (int i = internalList.size(); i < roomNumber - 1; i++) {
            Index index = Index.fromOneBased(i);
            Room roomToAdd = new Room(index.getOneBased());
            internalList.add(roomToAdd);
            rooms.add(room);
        }
    }

    private void setRoomForRoomNumberMoreThanNumberOfRooms(Room room) {
        int roomNumber = room.getRoomNumber();
        Index index = Index.fromOneBased(roomNumber);
        Room currRoom = internalList.get(index.getZeroBased());
        internalList.remove(index.getZeroBased());
        rooms.remove(currRoom);
        internalList.add(index.getZeroBased(), room);
        rooms.add(room);
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
        return internalList.size();
    }

    public ObservableList<Room> getRoomObservableList() {
        return internalList;
    }

    /**
     * Adds this room to the RoomList
     *
     * @param room is added to RoomList
     */
    public void initRooms(Room room) {
        rooms.add(room);
        internalList.add(room);
    }

    /**
     * Adds the number of the rooms in a hotel
     *
     * @param numOfRooms is the number of rooms to be added
     */
    public void initRooms(int numOfRooms) {
        this.numOfRooms = numOfRooms;
        initRooms();
    }

    private void initRooms() {
        if (numOfRooms <= 0) {
            return;
        } else if (numOfRooms > internalList.size()) {
            setRoomsInHotel_increaseInNumberOfRooms();
        } else if (numOfRooms < internalList.size()) {
            setRoomsInHotel_decreaseInNumberOfRooms();
        } else {
            //would not reach this block, exists to improve code quality
        }
    }

    private void setRoomsInHotel_increaseInNumberOfRooms() {
        ArrayList<Room> roomArrayList = new ArrayList<>(internalList);
        PriorityQueue<Room> rooms = new PriorityQueue<>(new ComparableRoom());

        this.rooms = new PriorityQueue<>(new ComparableRoom()); //remove all the rooms in the PQ
        for (int i = internalList.size(); i < numOfRooms; i++) {
            Room room = new Room(i + 1);
            rooms.add(room);
            roomArrayList.add(room);
        }
        this.rooms.addAll(rooms);
        internalList.setAll(roomArrayList);
    }

    private void setRoomsInHotel_decreaseInNumberOfRooms() {
        ArrayList<Room> roomArrayList = new ArrayList<>(internalList);
        PriorityQueue<Room> rooms = new PriorityQueue<>(new ComparableRoom());

        List<Room> occupiedRooms = occupiedRooms();
        List<Room> unoccupiedRooms = unOccupiedRooms();
        if (occupiedRooms.size() == 0) {

        } else {
            combinedStream(occupiedRooms, unoccupiedRooms);
        }
        this.rooms = new PriorityQueue<>(new ComparableRoom()); //remove all the rooms in the PQ
        for (int i = 0; i < numOfRooms; i++) {
            Room room = internalList.get(i);
            rooms.add(room);
        }
        int size = internalList.size();
        for (int i = numOfRooms; i < size; i++) {
            roomArrayList.remove(numOfRooms);
        }
        this.rooms.addAll(rooms);
        internalList.setAll(roomArrayList);
    }

    /**
     * Gives list of empty rooms in the region which exists after shrinkage.
     */
    public List<Room> unOccupiedRooms() {
        List<Room> rooms = new ArrayList<>();
        for (int i = 0; i < numOfRooms; i++) {
            if (!internalList.get(i).isOccupied()) {
                rooms.add(internalList.get(i));
            }
        }
        return rooms;
    }

    /**
     * Gives list of occupied rooms in the region which does not exist after shrinkage.
     */
    public List<Room> occupiedRooms() {
        List<Room> rooms = new ArrayList<>();
        for (int i = numOfRooms; i < internalList.size(); i++) {
            if (internalList.get(i).isOccupied()) {
                rooms.add(internalList.get(i));
            }
        }

        return rooms;
    }

    private void combinedStream(List<Room> occupiedRooms, List<Room> unoccupiedRooms) {
        for (Room room : occupiedRooms) {
            Room empty = unoccupiedRooms.get(0);
            unoccupiedRooms.remove(0);
            empty.setOccupied(true);
            empty.setPatient(room.getPatient().get());
            empty.addTasks(room.getReadOnlyTasks());
        }
    }

    public boolean hasSpaceForRooms() {
        return getNumOfExcessOccupiedRooms() <= numOfEmptyRooms();
    }

    /**
     * Gives the number of rooms that are occupied in the hotel facility beyond shrinkage
     */
    public int getNumOfExcessOccupiedRooms() {
        return (int) IntStream.rangeClosed(numOfRooms, internalList.size() - 1)
                .mapToObj(x -> internalList.get(x)).filter(Room::isOccupied).count();
    }

    /**
     * Gives the number of rooms that are empty for room numbers after shrinkage.
     */
    public int numOfEmptyRooms() {
        return (int) IntStream.rangeClosed(0, numOfRooms - 1)
                .mapToObj(x -> internalList.get(x)).filter(room -> !room.isOccupied()).count();
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Room> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        return equals(new PriorityQueue<>(rooms), new PriorityQueue<>(((UniqueRoomList) other).rooms))
                && internalList.equals(((UniqueRoomList) other).internalList);
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
        return internalList.stream().anyMatch(toCheck::isSameRoom);
    }

    //@@author chiamyunqing
    /**
     * Clears the room which contains the patient with the given name.
     * Tasks should still remain in the room.
     * @param patientName to clear the room from.
     */
    public void clearRoom(Name patientName) {
        requireNonNull(patientName);
        for (int i = 1; i <= internalList.size(); i++) {
            if (!internalList.get(i - 1).isOccupied()) {
                continue;
            }
            Name patientNameInRoom = internalList.get(i - 1).getPatient().get().getName();
            if (patientName.equals(patientNameInRoom)) {
                Room roomToClear = internalList.get(i - 1);
                setSingleRoom(roomToClear, new Room(roomToClear.getRoomNumber(),
                        false, Optional.empty(), new RoomTasks(roomToClear.getReadOnlyTasks())));
                break;
            }
        }
    }
    //@@author chiamyunqing

    /**
     * Replaces the room {@code target} in the list with {@code editedRoom}.
     * {@code target} must exist in the list.
     * The room identity of {@code editedRoom} must not be the same as another existing room in the list.
     *
     * @param target Room to be changed.
     * @param editedRoom Room that has been changed.
     */
    public void setSingleRoom(Room target, Room editedRoom) {
        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new RoomNotFoundException();
        }

        if (!target.isSameRoom(editedRoom) && containsRoom(editedRoom)) {
            throw new DuplicateRoomException();
        }
        rooms.remove(target); // this and the next LOC is to replace the room in the priority queue
        rooms.add(editedRoom);
        internalList.set(index, editedRoom);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(numOfRooms, rooms, internalList);
        result = 31 * result;
        return result;
    }

    public void setPreferredNumOfRooms(int numOfRooms) {
        this.numOfRooms = numOfRooms;
    }

    public void setRooms(PriorityQueue<Room> rooms) {
        this.rooms = rooms;
    }

}
