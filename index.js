'use strict'

var React = require('react-native')
var { DeviceEventEmitter, NativeAppEventEmitter, Platform } = React

var RNZipArchive = React.NativeModules.RNZipArchive
var promisify = require("es6-promisify")

var _unzip = promisify(RNZipArchive.unzip)
var _zip = promisify(RNZipArchive.zip)
var _unzipWithPassword = promisify(RNZipArchive.unzipWithPassword)
var _unzipAssets = RNZipArchive.unzipAssets ? promisify(RNZipArchive.unzipAssets) : undefined

var _error = (err) => {
  throw err
}

var ZipArchive = {
  unzip(source, target) {
    return _unzip(source, target)
      .catch(_error)
  },
  unzipWithPassword(source, target, password) {
    return _unzipWithPassword(source, target, password)
      .catch(_error)
  },
  zip(source, target) {
    return _zip(source, target)
      .catch(_error)
  },
  zip(source, target, password) {
    return _zip(source, target, password)
      .catch(_error)
  },
  unzipAssets(source, target) {
    if (!_unzipAssets) {
      throw new Error("unzipAssets not supported on this platform");
    }

    return _unzipAssets(source, target)
      .catch(_error)
  },
  subscribe(callback) {
    var emitter = Platform.OS == 'ios' ? NativeAppEventEmitter : DeviceEventEmitter;
    return emitter.addListener("zipArchiveProgressEvent", callback);
  }
}

module.exports = ZipArchive
