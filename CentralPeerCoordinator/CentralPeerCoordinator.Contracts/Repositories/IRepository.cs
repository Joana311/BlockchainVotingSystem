namespace CentralPeerCoordinator.Contracts.Repositories;

public interface IRepository<TEntity> where TEntity : class
{
    Task<List<TEntity>> GetAllAsync();

    Task<TEntity?> GetAsync(Guid id);

    void Add(TEntity entity);

    void Update(TEntity entity);

    void Remove(TEntity entity);
}

