var express = require("express");
var app = express();
var server = require("http").createServer(app);
var io = require("socket.io").listen(server);
var fs = require("fs");


var Room = require('./room.js');
// import { v4 as uuidv4 } from './uuid';
//var uuid = require('node-uuid');
const { v4: uuidv4 } = require('uuid');

server.listen(process.env.PORT || 3000);

var rooms = {};
var people = {};
var socketsX = {};

var arrayUser = ['a','b','c','d'];
var arrayUserLogined = [];


io.on("connection", function (client) {
    console.log("new connection going on");
    client.on("c-2-s-login", function(name){
        if(arrayUser.indexOf(name) > -1){

            people[client.id] = {"name" : name, "room" : []};//name;
            socketsX[name] = client;
            client.emit('s-2-c-login-get-contacts-list',{contactsList:arrayUser});
            if(!(arrayUserLogined.indexOf(name) > -1)){
                arrayUserLogined.push(name);
            }
            client.emit("s-2-c-update-status-user-joined", {userNameLoginedList:arrayUserLogined});
            io.sockets.emit("s-2-c-update-status-user-joined-newLogin", {userName:name});
            console.log(arrayUserLogined);
        }else{
            client.emit('s-2-c-login-username-not-existing-in-contacts-list',{message:"Tên này không có trong database."});
        }

    });

    client.on("c-2-s-send-chat", function(msg){

        var i = msg.indexOf("*");
        var friendName = msg.substring(0,i);
        var msgX = msg.substring(i+1);

        if(socketsX[friendName] != null){
            socketsX[friendName].emit('s-2-c-send-chat',{username: people[client.id].name,message:msgX,time:new Date().toLocaleString()});
        }
        

    });
    client.on("c-2-s-create-room-chat", function (roomName) {
        var id = uuidv4();console.log("RoomId-creator: "+id);
        var room = new Room(roomName, id, client.id);
        rooms[id] = room;
        room.addPerson(client.id);
        client.join(room.name);
        people[client.id].room.push(room);
        client.emit('s-2-c-create-room-chat',{rooId:id,roomName:roomName});
    });
    client.on("c-2-s-invite-into-room-chat", function (data) {
        //console.log(data.roomName);
        var roomName = data.roomName;
        var roomId = data.roomId;
        var friendName = data.friendName;

        socketsX[friendName].emit('s-2-c-invite-into-room-chat',{roomName:roomName,roomId:roomId,inviter:people[client.id].name});
    });
    client.on("c-2-s-accept-invite-join-room", function (data) {
        //console.log(data.roomName);
        var roomName = data.roomName;
        var roomId = data.roomId;
        var friendName = data.friendName;
        var room = rooms[roomId];
        console.log("RoomId-inviter: "+roomId);
        room.addPerson(client.id);
        //start try print number of room of owner
        console.log("room cua owner: "+people[rooms[roomId].owner].room);
        console.log("So room cua owner: "+people[rooms[roomId].owner].room.length);
        //end try print number of room of owner
        var friendNames = [];//username cua tat ca cac user trong room nay
        rooms[roomId].people.forEach(function (num) { friendNames.push(people[num].name) });

        socketsX[friendName].join(room.name);
        //socketsX[friendName].emit('s-2-c-accept-invite-join-room',{roomName:roomName,roomId:roomId,friendNames:friendNames});
        io.in(room.name).emit('s-2-c-accept-invite-join-room',{roomName:roomName,roomId:roomId,friendNames:friendNames});


    });
    client.on("c-2-s-send-chat-room",function (data) {
        var roomName = data.roomName;
        var roomId = data.roomId;
        var room = rooms[roomId];
        var message = data.msg;
        //io.in(room.name).emit('s-2-c-accept-invite-join-room',{roomName:roomName,roomId:roomId,friendNames:friendNames});
        client.broadcast.to(room.name).emit('s-2-c-send-chat-room',{roomName: roomName,username: people[client.id].name,message:message,time:new Date().toLocaleString()});

    });
    client.on("c-2-s-typing-chat", function (friendName) {
        if(socketsX[friendName] != null){
            socketsX[friendName].emit('s-2-c-typing-chat',{friendName:friendName});    
        }
        
    });
    client.on("c-2-s-cancel-typing-chat", function (friendName) {
        if(socketsX[friendName] != null){
            socketsX[friendName].emit('s-2-c-cancel-typing-chat',{friendName:friendName});
        }
    });
    //kiểm tra friend đã nằm trong room chat trước khi mời lần thứ 2
/////////////////////////////////////////////////////////////////////////////////////////////////////////

    client.on("disconnect", function(){
        io.sockets.emit("update", people[client.id] + " has left the server.");
        delete people[client.id];
        io.sockets.emit("update-people", people);
    });
});
