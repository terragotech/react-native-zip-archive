//
//  RNZipArchive.m
//  RNZipArchive
//
//  Created by Perry Poon on 8/26/15.
//  Copyright (c) 2015 Perry Poon. All rights reserved.
//

#import "RNZipArchive.h"

#import <React/RCTBridge.h>
#import <React/RCTEventDispatcher.h>

@implementation RNZipArchive

@synthesize bridge = _bridge;

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(unzip:(NSString *)zipPath destinationPath:(NSString *)destinationPath resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {

    if (![SSZipArchive isFilePasswordProtectedAtPath:zipPath]) {
        [self zipArchiveProgressEvent:0 total:1]; // force 0%
        
        BOOL success = [SSZipArchive unzipFileAtPath:zipPath toDestination:destinationPath delegate:self];
        
        [self zipArchiveProgressEvent:1 total:1]; // force 100%
        
        if (success) {
            resolve([NSNull null]);
        } else {
            reject(@"zip_error", @"unable to zip", NULL);
        }
        
    } else {
        reject(@"zip_error", @"unable to zip", NULL);
    }

}

RCT_EXPORT_METHOD(unzipWithPassword:(NSString *)zipPath destinationPath:(NSString *)destinationPath password:(NSString *)password resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {

    [self zipArchiveProgressEvent:0 total:1]; // force 0%

    BOOL success = [SSZipArchive unzipFileAtPath:zipPath toDestination:destinationPath password:password delegate:self];

    [self zipArchiveProgressEvent:1 total:1]; // force 100%

    if (success) {
        resolve([NSNull null]);
    } else {
         reject(@"zip_error", @"unable to zip", NULL);
    }
}

RCT_EXPORT_METHOD(zip:(NSString *)zipPath destinationPath:(NSString *)destinationPath resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)  {
    
    [self zipArchiveProgressEvent:0 total:1]; // force 0%
    
    BOOL success = [SSZipArchive createZipFileAtPath:destinationPath withContentsOfDirectory:zipPath];
    
    [self zipArchiveProgressEvent:1 total:1]; // force 100%
    
    if (success) {
        resolve([NSNull null]);
    } else {
        reject(@"zip_error", @"unable to zip", NULL);
    }
}

RCT_EXPORT_METHOD(zipWithPassword:(NSString *)zipPath destinationPath:(NSString *)destinationPath Password:(NSString*)password resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    
    
    BOOL isDir;
    BOOL exists = [[NSFileManager defaultManager] fileExistsAtPath:zipPath isDirectory:&isDir];
    if (!exists || !isDir) {
        NSMutableDictionary *responseDict = [[NSMutableDictionary alloc]init];
        [responseDict setObject:[NSNumber numberWithBool:false] forKey:@"isSuccess"];
        [responseDict setObject:@"Source folder not found." forKey:@"response"];
        reject(@"zip_error", @"unable to zip", responseDict);
        return;
    }
    
    [self zipArchiveProgressEvent:0 total:1]; // force 0%
    
    BOOL success = [SSZipArchive createZipFileAtPath:destinationPath withContentsOfDirectory:zipPath keepParentDirectory:TRUE withPassword:password];
    
    if (success) {
        NSMutableDictionary *responseDict = [[NSMutableDictionary alloc]init];
        [responseDict setObject:[NSNumber numberWithBool:true] forKey:@"isSuccess"];
        [responseDict setObject:@"success" forKey:@"response"];
        resolve(responseDict);
    } else {
        NSMutableDictionary *responseDict = [[NSMutableDictionary alloc]init];
        [responseDict setObject:[NSNumber numberWithBool:false] forKey:@"isSuccess"];
        [responseDict setObject:@"Error." forKey:@"response"];
        reject(@"zip_error", @"unable to zip", responseDict);
    }
    [self zipArchiveProgressEvent:1 total:1]; // force 100%
}


- (void)zipArchiveProgressEvent:(NSInteger)loaded total:(NSInteger)total {
    if (total == 0) {
        return;
    }

    // TODO: should send the filename, just like the Android version
    [self.bridge.eventDispatcher sendAppEventWithName:@"zipArchiveProgressEvent" body:@{
                                                                                        @"progress": @( (float)loaded / (float)total )
                                                                                        }];
}

@end
