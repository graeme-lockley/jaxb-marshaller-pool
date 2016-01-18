package za.co.no9.utils.jaxb;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Pool<T> {
    private final List<T> members = new ArrayList<>();
    private final MemberSupplier<T> memberSupplier;

    public Pool(MemberSupplier<T> memberSupplier) {
        this.memberSupplier = memberSupplier;
    }

    public T activate() throws JAXBException {
        synchronized (members) {
            if (members.isEmpty()) {
                return memberSupplier.get();
            } else {
                return members.remove(members.size() - 1);
            }
        }
    }

    public void passivate(T member) {
        synchronized (members) {
            if (member != null) {
                members.add(member);
            }
        }
    }

    public Stream<T> stream() {
        return members.stream();
    }
}
