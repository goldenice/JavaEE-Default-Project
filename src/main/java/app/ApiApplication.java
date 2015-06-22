package app;

import commons.crossorigin.CrossOriginAllowed;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@CrossOriginAllowed
@ApplicationPath("/api")
public class ApiApplication extends Application {}
