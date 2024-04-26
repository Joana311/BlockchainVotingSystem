using CentralPeerCoordinator.Contracts.Entities;
using CentralPeerCoordinator.Contracts.Repositories;

namespace CentralPeerCoordinator.Contracts.UoW;

public interface IUnitOfWork
{
    IRepository<Peer> Peers { get; }

    Task SaveChangesAsync();
}
