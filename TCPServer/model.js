/**
 * Created by daddyslab on 2016. 11. 27..
 */
'use strict';

var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var dataSchema = new Schema({
    pos1: {
        type: Boolean,
        default: false
    },
    pos2: {
        type: Boolean,
        default: false
    },
    pos3: {
        type: Boolean,
        default: false
    },
    pos4: {
        type: Boolean,
        default: false
    }
});

mongoose.model('Data', dataSchema);