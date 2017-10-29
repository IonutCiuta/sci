package com.ionut.ciuta.sci1hw.service;

import com.ionut.ciuta.sci1hw.exception.ResourceNotFound;
import com.ionut.ciuta.sci1hw.exception.ResourceOpertaionNotPermitted;
import com.ionut.ciuta.sci1hw.exception.UnauthorizedUser;
import com.ionut.ciuta.sci1hw.model.File;
import com.ionut.ciuta.sci1hw.model.Folder;
import com.ionut.ciuta.sci1hw.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceAccessService {
    @Autowired
    private AuthService authService;

    @Autowired
    private ResourceService resourceService;

    public String read(String user, String pass, String file) {
        if(!authService.isAuthenticated(user, pass)) {
            throw new UnauthorizedUser();
        }

        Resource resource = resourceService.find(file);

        if(resource == null) {
            throw new ResourceNotFound();
        }

        if(resource.owner.equals(user)) {
            return getContent(resource);
        } else {
            if(resource.permission.equals(Resource.Permission.R) ||
                    resource.permission.equals(Resource.Permission.RW)) {
                return getContent(resource);
            } else {
                throw new ResourceOpertaionNotPermitted();
            }
        }
    }

    private String getContent(Resource resource) {
        switch (resource.type) {
            case Resource.Type.FOLDER:
                return getFolderContent((Folder)resource);

            case Resource.Type.FILE:
                return ((File)resource).content;

            default:
                throw new UnsupportedOperationException();
        }
    }

    private String getFolderContent(Folder folder) {
        StringBuilder sb = new StringBuilder();

        folder.content.forEach(r -> {
            switch (r.type) {
                case Resource.Type.FILE:
                    sb.append(((File)r).content);
                    break;

                case Resource.Type.FOLDER:
                    sb.append(r.name).append("/");
                    break;
            }

            sb.append(" ");
        });

        return sb.toString();
    }
}