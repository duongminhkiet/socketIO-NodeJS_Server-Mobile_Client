/**
 * Created by duongminhkiet on 4/7/17.
 */
var express = require("express");
var app = express();
var server = require("http").createServer(app);
var io = require("socket.io").listen(server);
var fs = require("fs");
var Room = require('./room.js');
var uuid = require('node-uuid');

server.listen(process.env.PORT || 3000);

var people = {};
var rooms = {};
var clients = [];

socket.on("connection", function (client) {
    console.log("đã có một kết nối mới đến...");

    client.on("join", function(name) {
            roomID = null;
            people[client.id] = {"name" : name, "room" : roomID};
            client.emit("update", "You have connected to the server.");
            socket.sockets.emit("update", people[client.id].name + " is online.")
            socket.sockets.emit("update-people", people);
            client.emit("roomList", {rooms: rooms});
            clients.push(client); //populate the clients array with the client object
        });
    client.on("createRoom", function(name) {
        if (people[client.id].room === null) {
            var id = uuid.v4();
            var room = new Room(name, id, client.id);
            rooms[id] = room;
            socket.sockets.emit("roomList", {rooms: rooms}); //update the list of rooms on the frontend
            client.room = name; //name the room
            client.join(client.room); //auto-join the creator to the room

            room.addPerson(client.id); //also add the person to the room object
            people[client.id].room = id; //update the room key with the ID of the created room
        } else {
            socket.sockets.emit("update", "You have already created a room.");
        }
    });
    client.on("joinRoom", function(id) {
        var room = rooms[id];
        if (client.id === room.owner) {
            client.emit("update", "You are the owner of this room and you have already been joined.");
        } else {
            room.people.contains(client.id, function(found) {
                if (found) {
                    client.emit("update", "You have already joined this room.");
                } else {
                    if (people[client.id].inroom !== null) { //make sure that one person joins one room at a time
                        client.emit("update", "You are already in a room ("+rooms[people[client.id].inroom].name+"), please leave it first to join another room.");
                    } else {
                        room.addPerson(client.id);
                        people[client.id].inroom = id;
                        client.room = room.name;
                        client.join(client.room); //add person to the room
                        user = people[client.id];
                        socket.sockets.in(client.room).emit("update", user.name + " has connected to " + room.name + " room.");
                        client.emit("update", "Welcome to " + room.name + ".");
                        client.emit("sendRoomID", {id: id});
                    }
                }
            });
        }
    });
});