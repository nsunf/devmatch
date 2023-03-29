package com.devmatch.constant;

public enum RequestType {
	REQUEST,
	PROGRESS,
	COMPLETE,
	CANCEL,
	EDIT
	// request, false 
	// request, true -> progress
	// progress, true
	// edit, false
	// edit, true
	// complete, false
	// complete, true
	// cancel, false
	// cancel, true
}
