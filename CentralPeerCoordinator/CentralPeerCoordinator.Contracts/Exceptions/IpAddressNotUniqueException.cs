namespace CentralPeerCoordinator.Contracts.Exceptions;

public class IpAddressNotUniqueException : Exception
{
    public IpAddressNotUniqueException(string message) : base(message)
    {
    }
}
