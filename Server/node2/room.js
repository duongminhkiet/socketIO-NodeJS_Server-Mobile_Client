/**
 * Created by duongminhkiet on 4/7/17.
 */
function Room(name, id, owner) {
    this.name = name;
    this.id = id;
    this.owner = owner;
    this.people = [];
    this.status = "available";
};

Room.prototype.addPerson = function(personID) {
    if (this.status === "available") {
        this.people.push(personID);
    }
};

module.exports = Room;