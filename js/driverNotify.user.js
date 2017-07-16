// ==UserScript==
// @name        DriverNotify
// @include     http://example.com
// @require     http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js
// @require     https://cdn.pubnub.com/sdk/javascript/pubnub.4.12.0.js
// @grant       GM_addStyle
// ==/UserScript==
/*- The @grant directive is needed to work around a design change
    introduced in GM 1.0.   It restores the sandbox.
*/
//--- Use jQuery to add the form in a "popup" dialog.

function tempAlert(msg,duration)
{
 var el = document.createElement("div");
 el.setAttribute("style","position:absolute;top:40%;left:20%;background-color:white;");
 el.innerHTML = msg;
 setTimeout(function(){
  el.parentNode.removeChild(el);
 },duration);
 document.body.appendChild(el);
}

console.log("Subscribe Driver Notification");

    pubnub = new PubNub({
        publishKey : 'pub-c-24317e97-b457-4711-8b10-6dd17aaefa3b',
        subscribeKey : 'sub-c-8696da1e-69ab-11e7-8127-0619f8945a4f'
    })
console.log("Connected");
    pubnub.addListener({
        status: function(s) {
        // handle status
        var category = s.category; //PNConnectedCategory
        var operation = s.operation; //PNSubscribeOperation
        var affectedChannels = s.affectedChannels; //The channels affected in the operation, of type array.
        var subscribedChannels = s.subscribedChannels; //All the current subscribed channels, of type array.
        var affectedChannelGroups = s.affectedChannelGroups; //The channel groups affected in the operation, of type array.
        var lastTimetoken = s.lastTimetoken; //The last timetoken used in the subscribe request, of type long.
        var currentTimetoken = s.currentTimetoken; //The current timetoken fetched in the subscribe response, which is going to be used in the next request, of type long.
    },
        message: function(message) {
          var newMsg = message;
          console.log("New Message! ", newMsg.message);
          // $.notify(newMsg.message.text);
          tempAlert(newMsg.message, 10000);
        },
        presence: function(presenceEvent) {
            // handle presence
        }
    })
    console.log("Subscribing driver channel");
    pubnub.subscribe({
        channels: ['driverChannel']
    });
