package messages;

import java.nio.file.Path;

public class Write {
	private Path path;
	public Path getPath() {
		return path;
	}
	public Write(Path path) {
		super();
		this.path = path;
	}
}
