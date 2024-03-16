namespace CentralPeerCoordinator.Contracts.Entities;

public class Peer
{
    public Guid Id { get; set; }

    public string IpAddress { get; set; } = default!;

    public int Port { get; set; }
}
