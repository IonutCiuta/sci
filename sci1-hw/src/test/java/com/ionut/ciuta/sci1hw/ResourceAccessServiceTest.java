package com.ionut.ciuta.sci1hw;

import com.ionut.ciuta.sci1hw.exception.ResourceNotFound;
import com.ionut.ciuta.sci1hw.exception.ResourceOperationNotPermitted;
import com.ionut.ciuta.sci1hw.exception.UnauthorizedUser;
import com.ionut.ciuta.sci1hw.model.File;
import com.ionut.ciuta.sci1hw.model.Folder;
import com.ionut.ciuta.sci1hw.model.InsertionPoint;
import com.ionut.ciuta.sci1hw.model.Resource;
import com.ionut.ciuta.sci1hw.service.AuthService;
import com.ionut.ciuta.sci1hw.service.ResourceAccessService;
import com.ionut.ciuta.sci1hw.service.ResourceService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class ResourceAccessServiceTest {
    private final String userBob = "bob";
    private final String userAlice = "alice";
    private final String userBobPass = "bob";
    private final String userBobFile = "file.bob";
    private final String userAliceFile = "file.alice";

    @InjectMocks
    private ResourceAccessService resourceAccessService;

    @Mock
    private ResourceService resourceService;

    @Mock
    private AuthService authService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = UnauthorizedUser.class)
    public void readResourceShouldFailWithUnauthorizedUser() throws Exception {
        when(authService.isAuthenticated(any(), any())).thenReturn(false);

        resourceAccessService.read("", "", "");
    }

    @Test(expected = ResourceNotFound.class)
    public void readResourceShouldFailWithResourceNotFound() throws Exception {
        when(authService.isAuthenticated(any(), any())).thenReturn(true);
        when(resourceService.find(any())).thenReturn(null);

        resourceAccessService.read(userBob, userBobPass, userBobFile);
    }

    @Test(expected = ResourceOperationNotPermitted.class)
    public void readFolderShouldFailForNoPermissions() throws Exception {
        Folder folder = new Folder(userAlice, "", userAlice);

        when(authService.isAuthenticated(any(), any())).thenReturn(true);
        when(resourceService.find(any())).thenReturn(folder);

        resourceAccessService.read(userBob, userBobPass, userAlice);
    }

    @Test(expected = ResourceOperationNotPermitted.class)
    public void readFileShouldFailForNoPermissions() throws Exception {
        Folder folder = new Folder(userAlice, "", userAlice);
        File file = new File(userAliceFile, "", "", userAlice);
        folder.content.add(file);

        when(authService.isAuthenticated(any(), any())).thenReturn(true);
        when(resourceService.find(any())).thenReturn(folder);

        resourceAccessService.read(userBob, userBobPass, userAliceFile);
    }

    @Test(expected = ResourceOperationNotPermitted.class)
    public void readFolderShouldFailForInsufficientPermissions() throws Exception {
        Folder folder = new Folder(userAlice, Resource.Permission.W, userAlice);

        when(authService.isAuthenticated(any(), any())).thenReturn(true);
        when(resourceService.find(any())).thenReturn(folder);

        resourceAccessService.read(userBob, userBobPass, userAlice);
    }

    @Test(expected = ResourceOperationNotPermitted.class)
    public void readFileShouldFailForInsufficienPermissions() throws Exception {
        Folder folder = new Folder(userAlice, "", userAlice);
        File file = new File(userAliceFile, Resource.Permission.W, "", userAlice);
        folder.content.add(file);

        when(authService.isAuthenticated(any(), any())).thenReturn(true);
        when(resourceService.find(any())).thenReturn(folder);

        resourceAccessService.read(userBob, userBobPass, userAliceFile);
    }

    @Test
    public void readEmptyFolderShouldPassForReadPermissions() throws Exception {
        Folder folder = new Folder(userAlice, Resource.Permission.R, userAlice);

        when(authService.isAuthenticated(any(), any())).thenReturn(true);
        when(resourceService.find(any())).thenReturn(folder);

        assertEquals("", resourceAccessService.read(userBob, userBobPass, userAlice));
    }

    @Test
    public void readFolderShouldPassForReadPermissions() throws Exception {
        Folder folder = new Folder(userAlice, Resource.Permission.R, userAlice);
        Folder subfolder = new Folder(userBob, Resource.Permission.R, userAlice);
        File file = new File(userAliceFile, Resource.Permission.R, userAliceFile, userAlice);
        folder.content.add(subfolder);
        folder.content.add(file);

        when(authService.isAuthenticated(any(), any())).thenReturn(true);
        when(resourceService.find(any())).thenReturn(folder);

        assertEquals(userBob.concat("/ ").concat(userAliceFile).concat(" "), resourceAccessService.read(userBob, userBobPass, userAlice));
    }

    @Test
    public void readFileShouldPassForReadPermissions() throws Exception {
        Folder folder = new Folder(userAlice, "", userAlice);
        File file = new File(userAliceFile, Resource.Permission.R, userAliceFile, userAlice);
        folder.content.add(file);

        when(authService.isAuthenticated(any(), any())).thenReturn(true);
        when(resourceService.find(any())).thenReturn(file);

        assertEquals(file.content, resourceAccessService.read(userBob, userBobPass, userAlice));
    }

    @Test(expected = UnauthorizedUser.class)
    public void writeShouldFailWithUnauthorizedUser() throws Exception {
        when(authService.isAuthenticated(any(), any())).thenReturn(false);

        resourceAccessService.write("", "", "", "");
    }

    @Test(expected = ResourceNotFound.class)
    public void writeResourceShouldFailWithResourceNotFound() throws Exception {
        when(authService.isAuthenticated(any(), any())).thenReturn(true);
        when(resourceService.find(any())).thenReturn(null);

        resourceAccessService.write(userBob, userBobPass, userBobFile, "");
    }

    @Test(expected = ResourceNotFound.class)
    public void writeFolderShouldFailWithResourceNotFound() throws Exception {
        when(authService.isAuthenticated(any(), any())).thenReturn(true);
        when(resourceService.find(any())).thenReturn(new Folder("", "", ""));

        resourceAccessService.write(userBob, userBobPass, userBobFile, "");
    }

    @Test(expected = ResourceOperationNotPermitted.class)
    public void writeShouldFailForNoPermissions() throws Exception {
        File file = new File(userAlice, Resource.Permission.R, userAliceFile, userAlice);

        when(authService.isAuthenticated(any(), any())).thenReturn(true);
        when(resourceService.find(any())).thenReturn(file);

        resourceAccessService.write(userBob, userBobPass, userAlice, userBobFile);
    }

    @Test
    public void writeShouldPassForWritePermission() throws Exception {
        File file = new File(userAlice, Resource.Permission.RW, userAliceFile, userAlice);

        when(authService.isAuthenticated(any(), any())).thenReturn(true);
        when(resourceService.find(any())).thenReturn(file);

        resourceAccessService.write(userBob, userBobPass, userAlice, userBobFile);
        assertEquals(userBobFile, resourceAccessService.read(userBob, userBobPass, userAlice));
    }

    @Test
    public void writeShouldPassForUserFile() throws Exception {
        File file = new File(userBob, Resource.Permission.W, userBobFile, userBob);

        when(authService.isAuthenticated(any(), any())).thenReturn(true);
        when(resourceService.find(any())).thenReturn(file);

        resourceAccessService.write(userBob, userBobPass, userAlice, userBobFile);
        assertEquals(userBobFile, resourceAccessService.read(userBob, userBobPass, userBob));
    }

    @Test(expected = UnauthorizedUser.class)
    public void changeRightsShouldFailWithUnauthorizedUser() throws Exception {
        when(authService.isAuthenticated(any(), any())).thenReturn(false);

        resourceAccessService.changeRights("", "", "", "");
    }

    @Test(expected = ResourceNotFound.class)
    public void changeRightsShouldFailWithResourceNotFound() throws Exception {
        when(authService.isAuthenticated(any(), any())).thenReturn(true);
        when(resourceService.find(any())).thenReturn(null);

        resourceAccessService.changeRights("", "", "", "");
    }

    @Test(expected = ResourceOperationNotPermitted.class)
    public void changeRightsShouldFailForNoPermissions() throws Exception {
        File file = new File(userAliceFile, Resource.Permission.NONE, userAliceFile, userAlice);

        when(authService.isAuthenticated(any(), any())).thenReturn(true);
        when(resourceService.find(any())).thenReturn(file);

        resourceAccessService.changeRights(userBob, userBobPass, userAliceFile, Resource.Permission.R);
    }

    @Test
    public void changeRightsShouldPassForUserFile() throws Exception {
        File file = new File(userAliceFile, Resource.Permission.W, userAliceFile, userAlice);

        when(authService.isAuthenticated(any(), any())).thenReturn(true);
        when(resourceService.find(any())).thenReturn(file);

        resourceAccessService.changeRights(userBob, userBobPass, userAliceFile, Resource.Permission.RW);
        assertEquals(userAliceFile, resourceAccessService.read(userBob, userBobPass, userAliceFile));
    }

    @Test
    public void createShouldPassForTheRightPermissionsAndPropeNewFile() throws Exception {
        Folder folder = new Folder("root", Resource.Permission.R, userBob);
        Folder subfolder = new Folder("folder", Resource.Permission.R, userBob);

        InsertionPoint insertionPoint = new InsertionPoint(subfolder, Collections.singletonList("newFile"));
        when(authService.isAuthenticated(any(), any())).thenReturn(true);
        when(resourceService.exists(any())).thenReturn(false);
        when(resourceService.find(any())).thenReturn(folder);
        when(resourceService.findParent(any(), any())).thenReturn(insertionPoint);
        when(resourceService.createResourceFromPath(any(), any(), any(), any())).thenReturn(subfolder);

        resourceAccessService.create(userBob, userBobPass, "newFile", "newFileContent", "rw");
        assertEquals("newFileContent", resourceAccessService.read(userBob, userBobPass, "newFile"));
    }
}
