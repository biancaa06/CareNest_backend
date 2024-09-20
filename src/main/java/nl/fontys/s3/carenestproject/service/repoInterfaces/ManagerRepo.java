package nl.fontys.s3.carenestproject.service.repoInterfaces;

import nl.fontys.s3.carenestproject.persistance.entity.ManagerEntity;

public interface ManagerRepo {
    public ManagerEntity getManagerById(long id);
    public ManagerEntity getManagerByEmail(String email);
    public boolean addManager(ManagerEntity manager);
    public ManagerEntity updateManager(ManagerEntity manager);
    public boolean deleteManagerById(long id);
    public int countManagers();
}
