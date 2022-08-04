package ai.ecma.nardabot.repository;

import ai.ecma.nardabot.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AttachmentRepo extends JpaRepository<Attachment, UUID> {
}
