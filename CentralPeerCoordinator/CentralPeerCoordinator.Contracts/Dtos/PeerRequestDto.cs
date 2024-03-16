namespace CentralPeerCoordinator.Contracts.Dtos;

public class PeerRequestDto
{
    public string IpAddress { get; set; } = default!;

    public int Port { get; set; }
}
